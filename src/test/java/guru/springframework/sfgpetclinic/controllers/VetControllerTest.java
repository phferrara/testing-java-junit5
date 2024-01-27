package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.ModelMapImpl;
import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.services.SpecialtyService;
import guru.springframework.sfgpetclinic.services.VetService;
import guru.springframework.sfgpetclinic.services.map.SpecialityMapService;
import guru.springframework.sfgpetclinic.services.map.VetMapService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class VetControllerTest {

    @Test
    void listVets() {
        // given
        SpecialtyService specialtyService = new SpecialityMapService();
        VetService vetService = new VetMapService(specialtyService);
        Speciality speciality1 = new Speciality("pédiatrie");
        Speciality speciality2 = new Speciality("oncologie");
        Speciality speciality3 = new Speciality("allergies");
        Vet vet1 = new Vet(1L, "John", "Doe", Set.of(speciality1));
        Vet vet2 = new Vet(2L, "Laura", "Hannigan", Set.of(speciality2, speciality3));
        vetService.save(vet1);
        vetService.save(vet2);

        VetController vetController = new VetController(vetService);
        ModelMapImpl model = new ModelMapImpl();

        // when
        String view = vetController.listVets(model);

        // then
        assertEquals("vets/index", view);
        Set<Vet> vets = (Set)model.getMap().get("vets");
        assertEquals(2, vets.size());
        List<String> firstNames = vets.stream()
                .map(Vet::getFirstName)
                .collect(Collectors.toList());
        List<String> expectedFirstNames = List.of("John", "Laura");
        assertTrue(firstNames.size() == expectedFirstNames.size()
                && firstNames.containsAll(expectedFirstNames) && expectedFirstNames.containsAll(firstNames));
    }
}