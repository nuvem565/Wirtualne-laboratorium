package com.example.accessingdatamysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.Optional;

@Controller
@RequestMapping(path="/SQL")
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CzujnikiRepository czujnikiRepository;

    @Autowired
    private StanowiskaRepository stanowiskaRepository;

    @Autowired
    private ZapisyRepository zapisyRepository;


    //STANOWISKA

    @GetMapping(path="/allStations") // SELECT wszystkie stanowiska
    public @ResponseBody Iterable<Stanowiska> getAllStations() {
        return stanowiskaRepository.findAll();
    }

    @GetMapping(path="/stationByID") // SELECT stanowiska o ID
    public @ResponseBody
    Optional<Stanowiska> getCrossingByID(@RequestParam int id) {
        return stanowiskaRepository.findById(id);
    }

    //CZUJNIKI

    @GetMapping(path="/allDevices") // SELECT wszystkie czujniki
    public @ResponseBody Iterable<Czujniki> getAllDevices() {
        return czujnikiRepository.findAll();
    }

    @GetMapping(path="/deviceByID") // SELECT czujniki o ID
    public @ResponseBody
    Optional<Czujniki> getDeviceByID(@RequestParam int id) {
        return czujnikiRepository.findById(id);
    }

    //ZAPISY

    @GetMapping(path="/allReadings") // SELECT wszystkie zapisty
    public @ResponseBody Iterable<Zapisy> getAllReadings() {
        return zapisyRepository.findAll();
    }

    @GetMapping(path="/readingByID") // SELECT zapis o ID
    public @ResponseBody
    Optional<Zapisy> getReadingByID(@RequestParam int id) {
        return zapisyRepository.findById(id);
    }

    @GetMapping(path="/readingByDeviceId") // SELECT zapisy o ID czujnika
    public @ResponseBody Iterable<Zapisy> getCrossingByName(@RequestParam int id) {
        return zapisyRepository.findByCzuId(id);
    }

    @PostMapping(path="/addReading") // Dodaj zapis
    public @ResponseBody String addNewReading (
            @RequestParam String zapOpis,
            @RequestParam Float zapWartosc,
            @RequestParam Date zapCzas,
            @RequestParam int czuId) {
        Zapisy z = new Zapisy();
        z.setCzuId(czuId);
        z.setZapCzas(zapCzas);
        z.setZapOpis(zapOpis);
        z.setZapWartosc(zapWartosc);
        zapisyRepository.save(z);
        return "Saved";
    }

    @GetMapping(path="/deleteReading") // DELETE zapis o ID
    public @ResponseBody String deleteReading(@RequestParam int id) {
        try {
            zapisyRepository.deleteById(id);
            return "Deleted ID: " + id;
        }
        catch (EmptyResultDataAccessException e){
            return "ID " + id + " not found!";
        }
    }

    @PostMapping(path="/updateReading") // UPDATE zapis o ID
    public @ResponseBody String updateReading (
            @RequestParam int id,
            @RequestParam String zapOpis,
            @RequestParam Float zapWartosc,
            @RequestParam Date zapCzas,
            @RequestParam int czuId) {
        Zapisy z = zapisyRepository.findByZapId(id);
        try {
            z.setCzuId(czuId);
            z.setZapCzas(zapCzas);
            z.setZapOpis(zapOpis);
            z.setZapWartosc(zapWartosc);
            zapisyRepository.save(z);
            return "Updated ID: " + id;
        }
        catch (NullPointerException e){
            return "ID " + id + " not found!";
        }
    }


}