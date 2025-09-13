package com.porter.saathi.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FAQService {

    @Autowired AssistantService assistantService;

    public byte[] answerDriverQuery(MultipartFile multipartFile, String language) throws Exception {
        return assistantService.answerFAQQuery(multipartFile, language);
    }
}
