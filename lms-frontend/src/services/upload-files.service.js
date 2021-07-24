import authHeader from "./auth-header";
import http from "./http-common";

class UploadFilesService {
  upload(file, id, onUploadProgress) {
    let formData = new FormData();
    let aH= authHeader();

    formData.append("file", file);

    return http.post("/upload", formData, id, {
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

  viewFiles(reqId){
    return  http.get("http://localhost:8080/api/auth/files/"+reqId, { headers: authHeader() })
  }

}

export default new UploadFilesService();
