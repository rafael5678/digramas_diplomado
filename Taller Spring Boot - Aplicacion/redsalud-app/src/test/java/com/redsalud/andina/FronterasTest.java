package com.redsalud.andina;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class FronterasTest {

    private static final String[] OTROS = {
            "com.redsalud.andina.agenda..",
            "com.redsalud.andina.teleconsulta..",
            "com.redsalud.andina.receta..",
            "com.redsalud.andina.interconsulta..",
            "com.redsalud.andina.historial..",
            "com.redsalud.andina.acceso..",
            "com.redsalud.andina.auditoria..",
            "com.redsalud.andina.reportes..",
            "com.redsalud.andina.sincronizacion.."
    };

    @Test
    void unModuloNoImportaElPaqueteDeOtro() {
        JavaClasses clases = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.redsalud.andina");
        for (String propio : OTROS) {
            String[] ajenos = java.util.Arrays.stream(OTROS).filter(p -> !p.equals(propio)).toArray(String[]::new);
            noClasses()
                    .that().resideInAPackage(propio)
                    .should().dependOnClassesThat().resideInAnyPackage(ajenos)
                    .check(clases);
        }
    }
}
