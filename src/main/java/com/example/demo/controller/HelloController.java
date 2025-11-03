package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greeting")
@Tag(name = "Greeting API", description = "API để gửi lời chào") // Nhóm các API lại
public class HelloController {

    @Operation(summary = "Gửi lời chào tới một người", description = "Trả về chuỗi chào mừng dựa trên tên được cung cấp.", tags = {"Greeting API"})
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = "text/plain"))
    @GetMapping("/hello")
    public String sayHello(
        @Parameter(description = "Tên của người cần chào", example = "Gemini")
        @RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }
}