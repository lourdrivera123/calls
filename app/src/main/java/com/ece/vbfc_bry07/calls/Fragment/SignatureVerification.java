package com.ece.vbfc_bry07.calls.fragment;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;

public class SignatureVerification extends SignatureSpi {
    @Override
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {

    }

    @Override
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {

    }

    @Override
    protected void engineUpdate(byte b) throws SignatureException {

    }

    @Override
    protected void engineUpdate(byte[] b, int off, int len) throws SignatureException {

    }

    @Override
    protected byte[] engineSign() throws SignatureException {
        return new byte[0];
    }

    @Override
    protected boolean engineVerify(byte[] sigBytes) throws SignatureException {
        return false;
    }

    @Override
    protected void engineSetParameter(String param, Object value) throws InvalidParameterException {

    }

    @Override
    protected Object engineGetParameter(String param) throws InvalidParameterException {
        return null;
    }
}
