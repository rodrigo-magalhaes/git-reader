package com.rodrigo.gitreader.resource;

import com.rodrigo.gitreader.dto.InformationDTO;
import com.rodrigo.gitreader.model.Information;
import com.rodrigo.gitreader.service.InformationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/information")
public class InformationResource {

    private InformationService readerService;

    public InformationResource(InformationService readerService) {
        this.readerService = readerService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{repoAuthor}/{repoName}")
    public InformationDTO getRepositoryInformation(@PathVariable("repoAuthor") String repoAuthor,
                                                   @PathVariable("repoName") String repoName) {
        Information information = readerService.getRepositoryInformation(repoAuthor + "/" + repoName);
        return new InformationDTO(information);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handle() {
    }
}
