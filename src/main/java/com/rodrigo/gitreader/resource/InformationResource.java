package com.rodrigo.gitreader.resource;

import com.rodrigo.gitreader.dto.RepoInfoDTO;
import com.rodrigo.gitreader.model.RepoInfo;
import com.rodrigo.gitreader.service.InformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/information")
public class InformationResource {

    private InformationService readerService;

    public InformationResource(InformationService readerService) {
        this.readerService = readerService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{repoAuthor}/{repoName}")
    public RepoInfoDTO getRepositoryInformation(@PathVariable("repoAuthor") String repoAuthor,
                                                @PathVariable("repoName") String repoName) {
        RepoInfo information = readerService.getRepositoryInformation(repoAuthor + "/" + repoName);
        return new RepoInfoDTO(information);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error processing repository information")
    public void handle(Exception e) {
        //return a generic message not exposing internal state
    }
}
