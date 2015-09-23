package za.ac.cput.QuoteSystem.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import za.ac.cput.QuoteSystem.domain.AddJob;
import za.ac.cput.QuoteSystem.model.AddJobResource;
import za.ac.cput.QuoteSystem.services.AddJobService;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 2015/09/21.
 */
@RestController
@RequestMapping("/api/**")
public class AddJobHome {

    @Autowired
    private AddJobService service;

    @RequestMapping(value = "/jobs/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddJob>> listResponseEntity() {
        List<AddJob> addJobs = service.findAll();
        if(addJobs.isEmpty()){
            return new ResponseEntity<List<AddJob>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<AddJob>>(addJobs, HttpStatus.OK);
    }

    @RequestMapping(value = "/jobs/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddJob> getJobs(@PathVariable("id") long id)
    {
        AddJob addJob = service.findById(id);
        if(addJob == null)
        {
            return new ResponseEntity<AddJob>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AddJob>(addJob, HttpStatus.OK);
    }

    @RequestMapping(value = "/jobs/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createJobs(@RequestBody AddJob addJob, UriComponentsBuilder ucBuilder)
    {
        service.save(addJob);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/jobs/{id}").buildAndExpand(addJob.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jobs/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<AddJob> updateJob(@PathVariable("id") long id, @RequestBody AddJob newJob)
    {
        service.update(newJob);
        return new ResponseEntity<AddJob>(newJob, HttpStatus.OK);
    }

    @RequestMapping(value = "/jobs/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AddJob> deleteJob(@PathVariable("id") long id)
    {
        AddJob addJob = service.findById(id);
        if (addJob == null) {
            System.out.println("Unable to delete. PackageProduct with id " + id + " not found");
            return new ResponseEntity<AddJob>(HttpStatus.NOT_FOUND);
        }

        service.delete(addJob);
        return new ResponseEntity<AddJob>(HttpStatus.NO_CONTENT);
    }

   /* @RequestMapping(value="/job/", method= RequestMethod.GET)
    public List<AddJobResource> getAddJobs()
    {
        List<AddJobResource> hateos = new ArrayList<AddJobResource>();

        List<AddJob> addJobs = service.findAll();
        AddJobResource res;
        Link jobs;
        for(AddJob addJob: addJobs)
        {
             res = new AddJobResource.Builder(addJob.getJobName())
                    .resid(addJob.getId())
                    .vehicle(addJob.getVehicle())
                    .price(addJob.getPrice())
                    .jobDuration(addJob.getJobDuration())
                    .build();

            /*jobs = new Link ("http://localhost:8080/addJob/"+res.getResid().toString())
                    .withRel("job");


            jobs = (new

                    // create a link to this method on
                    Link(
                    linkTo(methodOn(AddJobHome.class).getAddJobs())
                            .slash(res.getResid()).toString()).withSelfRel()
            );
            res.add(jobs);
            hateos.add(res);

        }

       return hateos;
    }
               */
}
