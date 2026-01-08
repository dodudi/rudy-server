package kr.it.rudy.server.diff.presentation.controller;

import jakarta.validation.Valid;
import kr.it.rudy.server.common.dto.ApiResponse;
import kr.it.rudy.server.diff.application.dto.DiffRequest;
import kr.it.rudy.server.diff.application.dto.DiffResponse;
import kr.it.rudy.server.diff.application.service.DiffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diff")
public class DiffController {

    private final DiffService diffService;

    @PostMapping
    public ResponseEntity<ApiResponse<DiffResponse>> diff(@Valid @RequestBody DiffRequest request) {
        DiffResponse response = diffService.diff(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
