package com.nominaguate.service;

import com.nominaguate.dto.BoletaPagoVistaDTO;
import com.nominaguate.model.ConceptoNomina;
import com.nominaguate.model.Empleado;
import com.nominaguate.model.PeriodoPlanilla;
import com.nominaguate.model.PeriodoPlanilla.TipoPeriodo;
import com.nominaguate.model.PlanillaCabecera;
import com.nominaguate.model.PlanillaDetalle;
import com.nominaguate.model.PlanillaDetalleConcepto;
import com.nominaguate.repository.PlanillaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Reglas laborales de Guatemala para el cálculo de nómina.
 */
public class PlanillaService {

    // Constantes legales
    private static final BigDecimal PORCENTAJE_IGSS = new BigDecimal("0.0483");
    private static final BigDecimal BONIFICACION_INCENTIVO_MENSUAL = new BigDecimal("250.00");
    private static final BigDecimal FACTOR_HORA_EXTRA = new BigDecimal("1.5");
    private static final BigDecimal HORAS_JORNADA_DIARIA = new BigDecimal("8");
    private static final int DIAS_QUINCENA = 15;
    private static final int DIAS_MES = 30;
    private static final int ESCALA = 2;

    // ISR simplificado: estructura base, tramos oficiales pendientes de parametrizar
    private static final BigDecimal LIMITE_ISR_ANUAL = new BigDecimal("300000.00");
    private static final BigDecimal TASA_ISR_1 = new BigDecimal("0.05");
    private static final BigDecimal TASA_ISR_2 = new BigDecimal("0.07");
    private static final BigDecimal DEDUCCION_UNICA_ANUAL = new BigDecimal("48000.00");

    private final PlanillaRepository planillaRepository;

    public PlanillaService(PlanillaRepository planillaRepository) {
        this.planillaRepository = planillaRepository;
    }

    // Salario proporcional a los días realmente laborados
    public BigDecimal calcularSalarioOrdinario(BigDecimal salarioBase, BigDecimal diasLaborados,
                                                TipoPeriodo tipoPeriodo) {
        int diasPeriodo = diasDelPeriodo(tipoPeriodo);
        BigDecimal salarioDiario = salarioBase.divide(BigDecimal.valueOf(diasPeriodo), 6, RoundingMode.HALF_UP);
        return salarioDiario.multiply(diasLaborados).setScale(ESCALA, RoundingMode.HALF_UP);
    }

    // Monto de horas extra: tarifa hora normal * 1.5
    public BigDecimal calcularMontoHorasExtra(BigDecimal salarioBase, BigDecimal horasExtra,
                                               TipoPeriodo tipoPeriodo) {
        if (horasExtra == null || horasExtra.signum() <= 0) {
            return BigDecimal.ZERO.setScale(ESCALA, RoundingMode.HALF_UP);
        }
        int diasPeriodo = diasDelPeriodo(tipoPeriodo);
        BigDecimal tarifaHora = salarioBase
                .divide(BigDecimal.valueOf(diasPeriodo), 6, RoundingMode.HALF_UP)
                .divide(HORAS_JORNADA_DIARIA, 6, RoundingMode.HALF_UP);
        return tarifaHora.multiply(FACTOR_HORA_EXTRA).multiply(horasExtra)
                .setScale(ESCALA, RoundingMode.HALF_UP);
    }

    // Bono Decreto 78-89: completo en mensual, mitad en cada quincena
    public BigDecimal calcularBonificacionIncentivo(TipoPeriodo tipoPeriodo) {
        if (tipoPeriodo == TipoPeriodo.MENSUAL) {
            return BONIFICACION_INCENTIVO_MENSUAL;
        }
        return BONIFICACION_INCENTIVO_MENSUAL.divide(BigDecimal.valueOf(2), ESCALA, RoundingMode.HALF_UP);
    }

    // IGSS laboral 4.83% sobre el devengado imponible (bono incentivo exento)
    public BigDecimal calcularIgssLaboral(BigDecimal salarioDevengadoImponible) {
        return salarioDevengadoImponible.multiply(PORCENTAJE_IGSS).setScale(ESCALA, RoundingMode.HALF_UP);
    }

    // ISR laboral: proyecta el devengado a anual y aplica tramos
    public BigDecimal calcularIsr(BigDecimal salarioDevengadoImponible, TipoPeriodo tipoPeriodo) {
        int periodosPorAnio = tipoPeriodo == TipoPeriodo.MENSUAL ? 12 : 24;
        BigDecimal proyeccionAnual = salarioDevengadoImponible.multiply(BigDecimal.valueOf(periodosPorAnio));
        BigDecimal rentaImponible = proyeccionAnual.subtract(DEDUCCION_UNICA_ANUAL);

        if (rentaImponible.signum() <= 0) {
            return BigDecimal.ZERO.setScale(ESCALA, RoundingMode.HALF_UP); // exento
        }

        BigDecimal isrAnual;
        if (rentaImponible.compareTo(LIMITE_ISR_ANUAL) <= 0) {
            isrAnual = rentaImponible.multiply(TASA_ISR_1);
        } else {
            BigDecimal excedente = rentaImponible.subtract(LIMITE_ISR_ANUAL);
            isrAnual = LIMITE_ISR_ANUAL.multiply(TASA_ISR_1).add(excedente.multiply(TASA_ISR_2));
        }
        return isrAnual.divide(BigDecimal.valueOf(periodosPorAnio), ESCALA, RoundingMode.HALF_UP);
    }

    private int diasDelPeriodo(TipoPeriodo tipoPeriodo) {
        return tipoPeriodo == TipoPeriodo.MENSUAL ? DIAS_MES : DIAS_QUINCENA;
    }

    // Arma el detalle completo de un empleado dentro de la planilla
    public PlanillaDetalle calcularDetalleEmpleado(Empleado empleado, ResumenAsistenciaEmpleado resumen,
                                                    TipoPeriodo tipoPeriodo,
                                                    Map<String, ConceptoNomina> conceptosPorCodigo) {

        PlanillaDetalle detalle = new PlanillaDetalle(empleado.getIdEmpleado(), empleado.getSalarioActual());
        detalle.setDiasLaborados(resumen.diasLaborados());
        detalle.setHorasExtra(resumen.horasExtra());

        // Devengados
        BigDecimal salarioOrdinario = calcularSalarioOrdinario(
                empleado.getSalarioActual(), resumen.diasLaborados(), tipoPeriodo);
        BigDecimal montoHorasExtra = calcularMontoHorasExtra(
                empleado.getSalarioActual(), resumen.horasExtra(), tipoPeriodo);
        BigDecimal bonificacion = calcularBonificacionIncentivo(tipoPeriodo);

        detalle.setMontoHorasExtra(montoHorasExtra);
        detalle.setBonificacionIncentivo(bonificacion);

        // Base imponible IGSS/ISR: sin bonificación incentivo (exenta por ley)
        BigDecimal devengadoImponible = salarioOrdinario.add(montoHorasExtra);
        BigDecimal totalDevengado = devengadoImponible.add(bonificacion);
        detalle.setTotalDevengado(totalDevengado);

        // Descuentos
        BigDecimal igss = calcularIgssLaboral(devengadoImponible);
        BigDecimal isr = calcularIsr(devengadoImponible, tipoPeriodo);
        BigDecimal totalDescuentos = igss.add(isr);

        detalle.setIgssLaboral(igss);
        detalle.setIsr(isr);
        detalle.setTotalDescuentos(totalDescuentos);
        detalle.setSalarioNeto(totalDevengado.subtract(totalDescuentos));

        // Desglose auditable
        agregarConceptoSiAplica(detalle, conceptosPorCodigo, "HORAS-EXT", montoHorasExtra);
        agregarConceptoSiAplica(detalle, conceptosPorCodigo, "BONI-INC", bonificacion);
        agregarConceptoSiAplica(detalle, conceptosPorCodigo, "IGSS", igss);
        agregarConceptoSiAplica(detalle, conceptosPorCodigo, "ISR", isr);

        return detalle;
    }

    // Solo agrega la línea si el monto es mayor a cero
    private void agregarConceptoSiAplica(PlanillaDetalle detalle, Map<String, ConceptoNomina> conceptos,
                                          String codigo, BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) return;
        ConceptoNomina concepto = conceptos.get(codigo);
        if (concepto == null) return; // catálogo incompleto, se omite
        detalle.agregarConcepto(new PlanillaDetalleConcepto(concepto.getIdConcepto(), codigo, monto));
    }

    // Calcula y persiste la planilla completa de un periodo
    public PlanillaCabecera procesarPlanilla(PeriodoPlanilla periodo, List<Empleado> empleados,
                                              Map<Integer, ResumenAsistenciaEmpleado> resumenAsistencia,
                                              Integer idUsuarioGenero) throws SQLException {

        Map<String, ConceptoNomina> conceptosPorCodigo = planillaRepository.listarConceptosActivos();
        PlanillaCabecera cabecera = new PlanillaCabecera(periodo.getIdPeriodo(), idUsuarioGenero);

        for (Empleado empleado : empleados) {
            ResumenAsistenciaEmpleado resumen = resumenAsistencia.getOrDefault(
                    empleado.getIdEmpleado(),
                    new ResumenAsistenciaEmpleado(empleado.getIdEmpleado(), BigDecimal.ZERO, BigDecimal.ZERO));

            PlanillaDetalle detalle = calcularDetalleEmpleado(
                    empleado, resumen, periodo.getTipoPeriodo(), conceptosPorCodigo);
            cabecera.agregarDetalle(detalle);
        }

        consolidarTotales(cabecera);
        cabecera.setEstado(PlanillaCabecera.EstadoPlanilla.PROCESADA);

        return planillaRepository.procesarPlanilla(cabecera); // guarda todo en una transacción
    }

    // Suma los detalles y consolida los totales de cabecera
    private void consolidarTotales(PlanillaCabecera cabecera) {
        BigDecimal devengado = BigDecimal.ZERO;
        BigDecimal descuentos = BigDecimal.ZERO;
        BigDecimal neto = BigDecimal.ZERO;

        for (PlanillaDetalle detalle : cabecera.getDetalles()) {
            devengado = devengado.add(detalle.getTotalDevengado());
            descuentos = descuentos.add(detalle.getTotalDescuentos());
            neto = neto.add(detalle.getSalarioNeto());
        }

        cabecera.setTotalDevengado(devengado.setScale(ESCALA, RoundingMode.HALF_UP));
        cabecera.setTotalDescuentos(descuentos.setScale(ESCALA, RoundingMode.HALF_UP));
        cabecera.setTotalNeto(neto.setScale(ESCALA, RoundingMode.HALF_UP));
    }

    // Revierte una corrida completa
    public void anularPlanilla(int idPlanilla) throws SQLException {
        planillaRepository.anularPlanilla(idPlanilla);
    }

    public List<PeriodoPlanilla> listarPeriodosAbiertos() throws SQLException {
        return planillaRepository.listarPeriodosPorEstado(PeriodoPlanilla.EstadoPeriodo.ABIERTO);
    }

    public List<BoletaPagoVistaDTO> obtenerBoletasDePlanilla(int idPlanilla) throws SQLException {
        return planillaRepository.listarBoletasPorPlanilla(idPlanilla);
    }

    public Optional<BoletaPagoVistaDTO> obtenerBoleta(int idBoleta) throws SQLException {
        return planillaRepository.obtenerBoletaPorId(idBoleta);
    }
}