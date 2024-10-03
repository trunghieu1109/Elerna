package com.application.elerna.controller.v1;

import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.service.MailService;
import com.application.elerna.utils.ResponseExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/common")
@Tag(name="Email Service", description = "This is email service that allow to send message via email")
public class CommonController {

    private final MailService mailService;

    /**
     *
     * Send email via gmail
     *
     * @param recipients String
     * @param subject String
     * @param content String
     * @param files MultipartFile[]
     * @return ResponseData
     */
    @Operation(summary = "Send email", description = "Admin sends email",
            responses = { @ApiResponse(responseCode = "200", description = "Send email successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.sendEmailExample))
            )})
    @PostMapping("/send-email")
    public ResponseData<String> sendEmail(@RequestParam String recipients, @RequestParam String subject, @RequestParam String content, @RequestParam(required = false) MultipartFile[] files) {

        try {
            return new ResponseData<>(HttpStatus.OK, "Send message", mailService.sendEmail(recipients, subject, content, files));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseData<>(HttpStatus.BAD_REQUEST, "Cant send message");
        }
    }

}
