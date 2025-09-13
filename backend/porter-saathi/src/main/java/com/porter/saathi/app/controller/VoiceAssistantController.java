package com.porter.saathi.app.controller;

import com.porter.saathi.app.service.impl.AssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/assistant")
@CrossOrigin(origins = {"*"})
public class VoiceAssistantController {

    @Autowired
    private AssistantService assistantService;

    @PostMapping("/ask")
    public ResponseEntity<byte[]> askAssistant(
            @RequestParam(value = "driverId") String driverId,
            @RequestParam(value = "lang", required = false) String language,
            @RequestParam("file") MultipartFile audioFile) throws Exception {
        byte[] mp3Reply = assistantService.processQuery(audioFile, driverId, language);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=response.mp3")
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(mp3Reply);
    }

    @PostMapping("/form-error")
    public ResponseEntity<byte[]> formErrorToSpeech(@RequestParam("message") String errorMessage,
                                                    @RequestParam(value = "lang", required = false) String language) throws Exception {
        byte[] mp3Reply = assistantService.translateAndConvertToSpeech(errorMessage, language);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=form-error.mp3")
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(mp3Reply);
    }
}
