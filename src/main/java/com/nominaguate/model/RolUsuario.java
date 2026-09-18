package com.nominaguate.model;

/**
 * Roles del sistema. Determinan qué módulos y acciones puede ver/ejecutar
 * cada usuario (control de acceso simple basado en rol).
 */
public enum RolUsuario {
    ADMIN,    // Acceso total: usuarios, planillas, reportes, configuración
    RRHH,     // Gestión de empleados y planillas, acceso a reportes
    EMPLEADO  // Acceso limitado: solo su propia información/boletas
}
