package com.sprint.mission.discodeit.config.storage;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local", matchIfMissing = true)
@Slf4j
public class LocalConfig {

    @Value(value = "${STORAGE_LOCAL_ROOT_PATH:.discodeit/storage}")
    private static Path root;

    @PostConstruct
    void init(){

        log.debug("LocalStorage - local storage check - {}", root);

        if (Files.notExists(root)){
            try {

                Files.createDirectories(root);

                log.debug("LocalStorage - folder created - {}", root);

            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }


    /*
    데이터 입출력 매서드
     */
    static public void writeFile(String path, byte[] contents){
        try (BufferedOutputStream stream = output(path)) {
            stream.write(contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public void delete(String path) {
        try {
            Files.delete(resolve(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Stream 반환 매서드
     */
    static public BufferedOutputStream output(String path) throws IOException {
        return new BufferedOutputStream(Files.newOutputStream(resolve(path)));
    }

    static public BufferedInputStream input(String path) throws IOException {
        return new BufferedInputStream(Files.newInputStream(resolve(path)));
    }


    // 로컬 경로 resolve 용
    static private Path resolve(String path){
        return root.resolve(path);
    }

}
