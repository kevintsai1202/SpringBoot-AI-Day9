package com.example.ai.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptionRequest.GranularityType;
import org.springframework.ai.openai.audio.transcription.AudioTranscription;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class AiController {

	private final OpenAiAudioTranscriptionModel transcriptionModel;
	
	@PostMapping("/vtt")
    public ResponseEntity<String> videoToText(MultipartFile file) {
		OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
			    .withTemperature(0f)
			    .withResponseFormat(TranscriptResponseFormat.SRT)
			    .build();
		AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt( file.getResource(), transcriptionOptions);
		AudioTranscriptionResponse response = transcriptionModel.call(transcriptionRequest);        
		return ResponseEntity.ok()
        		.header(HttpHeaders.CONTENT_DISPOSITION, 
        				ContentDisposition
        					.attachment()
        					.filename("字幕檔.srt", StandardCharsets.UTF_8)
        					.build().toString()
        				)
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
        		.body(response.getResult().getOutput());
    }
}
