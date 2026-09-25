package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.config.storage.LocalConfig;
import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local", matchIfMissing = true)
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {

    @Override
    public UUID put(UUID id, byte[] content) {
        LocalConfig.writeFile(id.toString(), content);
        return id;
    }
    @Override
    public InputStream get(UUID id) throws IOException {
        return LocalConfig.input(id.toString());
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new InputStreamResource(LocalConfig.input(binaryContentDto.fileName()))
                    );
        } catch (IOException e){
            log.error("LocalBinaryContentStorage - 파일 리소스 응답 생성 에러 - {}",binaryContentDto.fileName());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(UUID id){
        LocalConfig.delete(id.toString());
    }

}
