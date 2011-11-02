/*
 * Copyright (C) 2008 Security Engineering Research Group, 
 * Institute of Management Sciences, Peshawar, Pakistan.
 * http://serg.imsciences.edu.pk 
 * authors: Nauman and Tamleek 
 */

package serg.tc.utils;

import java.io.*;

import iaik.tc.tss.api.exceptions.common.TcTssException;
import iaik.tc.tss.api.tspi.TcIContext;
import iaik.tc.utils.logging.Log;

import serg.mba.wsa.util.CommonSettings;

import iaik.tc.tss.api.constants.tsp.TcTssConstants;
import iaik.tc.tss.api.structs.common.TcBlobData;
import iaik.tc.tss.api.structs.tpm.TcTpmPubkey;
import iaik.tc.tss.api.structs.tsp.TcTssValidation;
import iaik.tc.tss.api.structs.tsp.TcUuidFactory;
import iaik.tc.tss.api.tspi.TcIPcrComposite;
import iaik.tc.tss.api.tspi.TcIPolicy;
import iaik.tc.tss.api.tspi.TcIRsaKey;
import iaik.tc.tss.api.tspi.TcITpm;
import iaik.tc.tss.impl.csp.TcCrypto;
import iaik.tc.utils.logging.Log;

import java.security.interfaces.RSAPublicKey;

public class Export {

	private static String BACLIENT_AIK_SECRET = "secret";
	private static String AIK_FILENAME = "/root/tpm-softs/trustjava/jTpmTools_0.3a/bac_aik.tpmkey";
	private static String AIK_PUB_FILENAME = "/root/tpm-softs/trustjava/jTpmTools_0.3a/bac_aik_pub.key";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// create a context for TSS
			TcIContext context = CommonSettings.getTssFactory()
					.newContextObject();

			context.connect(null); // connect to localhost

			byte blob[] = null;
			try {
				File f = new File(AIK_FILENAME);
				blob = new byte[(int) f.length()];
				FileInputStream fi = new FileInputStream(f);
				fi.read(blob);
			} catch (Exception e) {
				e.printStackTrace();
			}

			TcBlobData srkSecret = TcBlobData
					.newByteArray(TcTssConstants.TSS_WELL_KNOWN_SECRET);
			long srkSecretMode = TcTssConstants.TSS_SECRET_MODE_SHA1;

			TcIRsaKey srk = context.loadKeyByUuidFromSystem(TcUuidFactory
					.getInstance().getUuidSRK());

			// TcIPolicy srkPolicy = context
			// .createPolicyObject(TcTssConstants.TSS_POLICY_USAGE);
			TcIPolicy srkPolicy = srk.getUsagePolicyObject();
			srkPolicy.setSecret(srkSecretMode, srkSecret);
			srkPolicy.assignToObject(srk);

			// create a TcBlobData using
			TcBlobData keyBlob = TcBlobData.newByteArray(blob);

			// load the key using this blob
			TcIRsaKey identityKey = context.loadKeyByBlob(srk, keyBlob);

			TcIRsaKey pubAik = identityKey;
			TcBlobData pubAikBlob = pubAik.getAttribData(
					TcTssConstants.TSS_TSPATTRIB_KEY_BLOB,
					TcTssConstants.TSS_TSPATTRIB_KEYBLOB_PUBLIC_KEY);

			// ***********************************************************
			// write this pubAikBlob to a file and send it to the challenger
			try {
				File f = new File(AIK_PUB_FILENAME);
				byte[] pubKeyBytes = pubAikBlob.asByteArray();
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(pubKeyBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// ***********************************************************

			// the following two lines should be performed on the challenger
			// side
			// to get the rsa public key for verification
			// TcTpmPubkey pubAikStruct = new TcTpmPubkey(pubAikBlob);

			// RSAPublicKey rsaPub = TcCrypto.pubTpmKeyToJava(pubAikStruct);

			context.closeContext();
		} catch (TcTssException tse) {
			Log.err(tse.getMessage());
			tse.printStackTrace();
		} catch (Exception nse) {
			nse.printStackTrace();
		}

	}

}
