package org.acme;

import jakarta.persistence.*;
import java.io.File;

@Entity
@Table(name = "uploaded_files")
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String filename; 

    @Transient
    private File file; 

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}