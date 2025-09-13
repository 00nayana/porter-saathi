package com.porter.saathi.app.controller;


import com.porter.saathi.app.service.impl.AssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/conversation")
@CrossOrigin(origins = {"*"})
public class ConversationController {

    @Autowired
    private AssistantService assistantService;


    @PostMapping()
    public ResponseEntity<byte[]> askAssistant(
            @RequestParam(value = "language") String language,
            @RequestParam("file") MultipartFile audioFile) throws Exception {
        byte[] mp3Reply = assistantService.toDesiredLanguage(audioFile, language);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=response.mp3")
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(mp3Reply);
    }
}
