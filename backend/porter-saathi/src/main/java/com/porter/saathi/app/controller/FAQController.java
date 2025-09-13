package com.porter.saathi.app.controller;

import com.porter.saathi.app.service.impl.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/driver/faq")
@CrossOrigin(origins = {"*"})
public class FAQController {

    @Autowired
    private FAQService faqService;

    @PostMapping()
    public ResponseEntity<byte[]> answerDriverQuery(@RequestParam("language") String language,
                                                    @RequestParam("file") MultipartFile audioFile) throws Exception {

        byte[] response = faqService.answerDriverQuery(audioFile, language);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=form-error.mp3")
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(response);
    }

}
