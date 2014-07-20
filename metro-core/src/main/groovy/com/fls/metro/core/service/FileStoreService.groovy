package com.fls.metro.core.service

import com.fls.metro.core.util.FileUtils

import java.security.SecureRandom

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 16.05.14
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
class FileStoreService {

    private SecureRandom random = new SecureRandom();

    private String generateToken() {
        return new BigInteger(130, random).toString(32);
    }

    protected String getExtension(String fileName){
        FileUtils.getExtension(fileName)
    }


    private String generateFileName(String pathToStore, String originalFilename, String prefix,  String token){
        String extension=getExtension(originalFilename);
        return String.format("%s%s_%s%s",pathToStore, prefix, token, extension);
    }

    protected String generateUniqueFileName(String prefix, String path, String originalFileName){

        String filePath=null;
        File file=null;
        boolean uniqueImageName=false;
        while(!uniqueImageName){
            filePath= generateFileName(path, originalFileName, prefix, generateToken());
            file=new File(filePath);
            if(!file.exists()){
                uniqueImageName=true;
            }
        }
        return filePath;
    }

    protected String parseImageFileName(String url){
        int pos=url.lastIndexOf('/');
        if(pos<0){
            return url;
        }
        return url.substring(pos+1);
    }


    protected String getFilePathByUrl(String url, String storePath){
        return storePath+parseImageFileName(url);
    }
}
