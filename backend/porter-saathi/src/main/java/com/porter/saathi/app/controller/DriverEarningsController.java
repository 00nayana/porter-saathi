package com.porter.saathi.app.controller;

import com.porter.saathi.app.models.EarningsEntry;
import com.porter.saathi.app.service.impl.DriverEarningsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/driver/earnings")
@CrossOrigin(origins = {"*"})
public class DriverEarningsController {

    @Autowired
    private DriverEarningsService driverEarningsService;

    @GetMapping("/{id}")
    public Map<String, List<EarningsEntry>> getDriverEarnings(@PathVariable String id) {
        return driverEarningsService.getDriverEarnings(id);
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<byte[]> getDriverEarningsSummary(@PathVariable String id, @RequestParam String language) throws Exception {
        byte[] bytes = driverEarningsService.getDriverEarningsSummary(id, language);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=form-error.mp3")
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(bytes);
    }
}
