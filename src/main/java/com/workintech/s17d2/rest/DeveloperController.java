package com.workintech.s17d2.rest;

import com.workintech.s17d2.dto.DeveloperResponse;
import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.DeveloperFactory;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.objenesis.ObjenesisHelper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/developers")
public class DeveloperController {


    private Map<Integer, Developer> developers;

    private Taxable taxable;

   @Autowired
    public DeveloperController( Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public  void init(){
       developers = new HashMap<>();
    }


    @PostMapping
     @ResponseStatus(HttpStatus.CREATED)
    public DeveloperResponse save(@RequestBody Developer developer){
        Developer createDeveloper = DeveloperFactory.createDeveloper(developer,taxable);
        if(Objects.nonNull(createDeveloper)){
            developers.put(developer.getId(), createDeveloper);
        }
       return new DeveloperResponse(createDeveloper.getId(), createDeveloper.getName(), createDeveloper.getSalary(), createDeveloper.getExperience());
    }

    @GetMapping
    public List<Developer> getAll(){
       return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public DeveloperResponse get(@PathVariable Integer id){
       Developer developer = developers.get(id);
       return new DeveloperResponse(developer.getId(), developer.getName(), developer.getSalary(), developer.getExperience());
    }

    @PutMapping("/{id}")
    public  DeveloperResponse update(@PathVariable Integer id, @RequestBody Developer developer){
       if (Objects.isNull(developer)){
           return  new DeveloperResponse(null,null,null,null);
       }
       Developer updatedDeveloper = DeveloperFactory.createDeveloper(developer,taxable);
       this.developers.put(id, updatedDeveloper);
       return new DeveloperResponse(updatedDeveloper.getId(), updatedDeveloper.getName(), updatedDeveloper.getSalary(), updatedDeveloper.getExperience());

    }

    @DeleteMapping("/{id}")
    public DeveloperResponse delete (@PathVariable Integer id){
       Developer developer = developers.get(id);
       if(Objects.isNull(developer)){
           return new DeveloperResponse(null,null,null,null);
       }
       developers.remove(id);
       return  new DeveloperResponse(developer.getId(), developer.getName(), developer.getSalary(), developer.getExperience());

    }

}
