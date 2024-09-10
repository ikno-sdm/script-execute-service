package com.ikno.ikdata.external;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

@Service
public class FileService {

    public void writeToFile(Path path, byte[] content) throws IOException {
        Files.write(path, content);
    }

    public boolean deleteFileIfExists(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    public boolean fileExists(String scriptPath){
        return new File(scriptPath).exists();
    }
}