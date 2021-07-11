import authHeader from "./auth-header";
import http from "./http-common";

class UploadFilesService {
  upload(file, onUploadProgress) {
    let formData = new FormData();
    let aH= authHeader();

    formData.append("file", file);

    return http.post("/upload", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
        aH,
      },
      onUploadProgress,
    });
  }

  getFiles() {
    return http.get("/files");
  }

  viewFiles(url){
    return  http.get(url, { headers: authHeader() })
  }

}

export default new UploadFilesService();
