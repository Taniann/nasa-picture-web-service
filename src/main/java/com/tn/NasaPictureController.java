package com.tn;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class NasaPictureController {

    private final NasaLargestPictureService pictureService;

    @GetMapping("/{sol}/largest")
    public ResponseEntity<?> getLargestImage(@PathVariable("sol") int sol) {
        String largestPictureURl = pictureService.getLargestPictureURl(sol);

        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                             .location(URI.create(largestPictureURl))
                             .build();
    }
}
