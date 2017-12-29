package com.acmed.his.filter.servlet;

import okio.ByteString;
import okio.Okio;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-28
 **/
public class CustomServlteRequest extends HttpServletRequestWrapper {
    public CustomServlteRequest(ServletRequest request) {
        super((HttpServletRequest)request);
    }

    private ByteString byteString = null;
    @Override
    public ServletInputStream getInputStream() throws IOException{
        byteString = Optional.ofNullable(byteString).orElse(Okio.buffer(Okio.source(super.getInputStream())).readByteString());
        return new CustomServletInputStream(byteString.toByteArray());
    }

    private class CustomServletInputStream extends ServletInputStream  {

        ByteArrayInputStream stream = null;
        public CustomServletInputStream(byte[] bytes) {stream = new ByteArrayInputStream(bytes);}


        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }
    }
}
