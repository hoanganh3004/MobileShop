package com.library.MoileShop.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {

    private static final String BASE_DIR = "D:/image"; // thư mục ảnh gốc

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullPath = request.getParameter("file");

        if (fullPath == null || fullPath.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tên file");
            return;
        }

        // 👉 Chỉ lấy tên file từ đường dẫn tuyệt đối
        String fileName = new File(fullPath).getName(); // "ip1.jpg"

        // Load ảnh từ thư mục D:/image/
        File file = new File(BASE_DIR, fileName);

        if (!file.exists() || file.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy ảnh");
            return;
        }

        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLengthLong(file.length());

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}

