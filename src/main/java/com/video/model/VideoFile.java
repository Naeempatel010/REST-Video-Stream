package com.video.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class VideoFile {

@Id	
String name;

Long filesize;
String filename;

public String getName() {
	return name;
}
public VideoFile() {
	super();
	
}



public Long getFilesize() {
	return filesize;
}
public void setFilesize(Long filesize) {
	this.filesize = filesize;
}
public void setName(String name) {
	this.name = name;
}

public String getFilename() {
	return filename;
}

public void setFilename(String filename) {
	this.filename = filename;
}

public String getFiletype() {
	return filetype;
}

public void setFiletype(String filetype) {
	this.filetype = filetype;
}


public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

String filetype;
String filelocation;

public String getFilelocation() {
	return filelocation;
}
public void setFilelocation(String filelocation) {
	this.filelocation = filelocation;
}
@Lob
byte[] data;
public VideoFile(String name, Long filesize, String filename, String filetype, String filelocation, byte[] data,
		String description) {
	super();
	this.name = name;
	this.filesize = filesize;
	this.filename = filename;
	this.filetype = filetype;
	this.filelocation = filelocation;
	this.data = data;
	this.description = description;
}
public byte[] getData() {
	return data;
}
public void setData(byte[] data) {
	this.data = data;
}

String description;

	
}
