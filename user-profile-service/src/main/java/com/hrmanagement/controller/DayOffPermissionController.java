package com.hrmanagement.controller;

import com.hrmanagement.dto.request.TakeDayOffPermissionRequestDto;
import com.hrmanagement.repository.entity.DayOffPermission;
import com.hrmanagement.service.DayOffPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("api/v1/day-off-permission")
@RequiredArgsConstructor
public class DayOffPermissionController {
    private final DayOffPermissionService dayOffPermissionService;

    @PostMapping("/take-day-off-permission/{token}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<DayOffPermission> takeDayOffPermission(@PathVariable String token, @RequestBody TakeDayOffPermissionRequestDto dto) throws ParseException {
        return ResponseEntity.ok(dayOffPermissionService.takeDayOffPermission(token,dto));
    }
}
