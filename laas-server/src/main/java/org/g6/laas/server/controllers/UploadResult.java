package org.g6.laas.server.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResult {
    Long id;
    String fileName;
    Long size;
    String status;
}
