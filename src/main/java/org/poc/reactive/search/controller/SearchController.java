package org.poc.reactive.search.controller;

import org.poc.reactive.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/search", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> search() {
        return searchService.search();
    }

}
