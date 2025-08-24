package com.pepino.backend.service.impl;

import com.pepino.backend.service.DirectoryService;

import java.nio.file.Path;

public class DirectoryServiceImpl implements DirectoryService {
    private static final Path parentDirectory = Path.of("D:/");

    public Path createPath(Path path){
        return parentDirectory.resolve(path);
    }
}
