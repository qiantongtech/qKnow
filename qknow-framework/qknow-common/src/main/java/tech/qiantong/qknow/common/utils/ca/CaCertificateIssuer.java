/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
 */

package tech.qiantong.qknow.common.utils.ca;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.x500.X500Principal;
import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 证书颁发工具类，提供方法使用根证书和私钥签发用户证书。
 * 本工具类通过加载根证书和私钥，生成用户证书并返回MultipartFile列表。
 * @author qknow
 */
public class CaCertificateIssuer {

    /**
     * 签发用户证书并返回证书和私钥的 MultipartFile 列表
     *
     * @param userName 包含用户的详细信息
     * @param certUrl 根证书的 URL
     * @param privateKeyUrl 私钥的 URL
     * @param validity 证书的有效期（年）
     * @return List<MultipartFile> 包含用户证书和私钥的 MultipartFile 列表
     * @throws Exception 如果签发过程中发生错误
     */
    public static List<MultipartFile> issueCertificate(String userName, String certUrl,
                                                       String privateKeyUrl, Long validity) throws Exception {
        X500Principal userDnName = new X500Principal(userName);
        List<MultipartFile> fileList = new ArrayList<>();

        // 加载根证书和私钥
        X509Certificate rootCertificate = loadRootCertificate(certUrl);
        PrivateKey rootPrivateKey = loadRootPrivateKey(privateKeyUrl);

        // 获取根证书的主体信息，作为用户证书的颁发者信息
        X500Name issuerName = new X500Name(rootCertificate.getSubjectX500Principal().getName());

        // 生成用户的密钥对（公钥和私钥）
        KeyPair userKeyPair = generateUserKeyPair();
        PublicKey userPublicKey = userKeyPair.getPublic();
        PrivateKey userPrivateKey = userKeyPair.getPrivate();

        // 定义用户证书的主体信息
        X500Name subjectName = new X500Name(userDnName.getName());

        // 创建证书序列号
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

        // 定义证书有效期
        Date notBefore = new Date();
        Date notAfter = new Date(System.currentTimeMillis() + validity * 365L * 24L * 60L * 60L * 1000L);

        // 创建证书构建器
        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                issuerName, serial, notBefore, notAfter, subjectName, userPublicKey);

        // 添加扩展字段
        String cnValue = ReUtil.get("CN=([^,]+)", userDnName.getName(), 1);
        if (cnValue != null) {
            // 判断是否是IP地址或域名
            boolean isIpAddress = Validator.isIpv4(cnValue);
            boolean isDomain = ReUtil.isMatch("^(\\*\\.)?([\\w-]+\\.)+[a-zA-Z]{2,}$", cnValue);

            if (isIpAddress || isDomain) {
                GeneralName generalName;
                if (isIpAddress) {
                    generalName = new GeneralName(GeneralName.iPAddress, cnValue);
                } else {
                    generalName = new GeneralName(GeneralName.dNSName, cnValue);
                }

                GeneralNames generalNames = new GeneralNames(generalName);
                certificateBuilder.addExtension(Extension.subjectAlternativeName, false, generalNames);
            }
        }

        // 使用根证书的私钥签署用户证书
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA")
                .build(rootPrivateKey);
        X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);

        // 转换为标准 X509Certificate
        JcaX509CertificateConverter converter = new JcaX509CertificateConverter();
        X509Certificate userCertificate = converter.getCertificate(certificateHolder);

        // 将用户证书转换为 MultipartFile
        fileList.add(convertCertificateToMultipartFile(userCertificate, cnValue + "_certificate.cer"));

        // 将用户私钥保存为 PEM 文件并转换为 MultipartFile
        fileList.add(convertPrivateKeyToMultipartFile(userPrivateKey, cnValue + "_privateKey.pem"));

        return fileList;
    }

    /**
     * 生成 RSA 密钥对
     *
     * @return 生成的 RSA 密钥对
     * @throws Exception 如果密钥对生成失败
     */
    private static KeyPair generateUserKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 将 X.509 证书对象转换为 MultipartFile
     *
     * @param certificate X.509 证书对象
     * @param fileName 文件名
     * @return MultipartFile 形式的证书
     * @throws Exception 如果转换过程中发生错误
     */
    private static MultipartFile convertCertificateToMultipartFile(X509Certificate certificate, String fileName) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.US_ASCII))) {
            writer.write("-----BEGIN CERTIFICATE-----\n");
            writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(certificate.getEncoded()));
            writer.write("\n-----END CERTIFICATE-----\n");
        }
        return new MockMultipartFile(fileName, fileName, "application/x-x509-ca-cert", outputStream.toByteArray());
    }

    /**
     * 将私钥对象转换为 MultipartFile
     *
     * @param privateKey 私钥对象
     * @param fileName 文件名
     * @return MultipartFile 形式的私钥
     * @throws Exception 如果转换过程中发生错误
     */
    private static MultipartFile convertPrivateKeyToMultipartFile(PrivateKey privateKey, String fileName) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream, StandardCharsets.US_ASCII))) {
            PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
            pemWriter.writeObject(pemObject);
        }
        return new MockMultipartFile(fileName, fileName, "application/x-pem-file", outputStream.toByteArray());
    }

    /**
     * 从指定的 URL 加载根证书
     *
     * @param certUrl 根证书的 URL
     * @return 加载的 X509Certificate 对象
     * @throws Exception 如果加载过程中发生错误
     */
    public static X509Certificate loadRootCertificate(String certUrl) throws Exception {
        try (InputStream certStream = new URL(getServerIpAndPort() + certUrl).openStream()) {
            if (certStream == null) {
                throw new IllegalArgumentException("根证书文件未找到！");
            }
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certFactory.generateCertificate(certStream);
        }
    }

    /**
     * 从指定的 URL 加载私钥
     *
     * @param privateKeyUrl 私钥的 URL
     * @return 加载的 PrivateKey 对象
     * @throws Exception 如果加载过程中发生错误
     */
    public static PrivateKey loadRootPrivateKey(String privateKeyUrl) throws Exception {
        try (InputStream keyStream = new URL(getServerIpAndPort() + privateKeyUrl).openStream()) {
            if (keyStream == null) {
                throw new IllegalArgumentException("私钥文件未找到！");
            }
            PemReader pemReader = new PemReader(new InputStreamReader(keyStream));
            byte[] keyBytes = pemReader.readPemObject().getContent();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    /**
     * 获取当前后端服务器的 IP 和端口
     *
     * @return 服务器的 IP 和端口，格式为 "IP:端口"
     */
    public static String getServerIpAndPort() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取服务器的 IP 和端口
        int serverPort = request.getLocalPort();

        return "http://127.0.0.1" + ":" + serverPort;
    }

    public static void main(String[] args) throws Exception {
        // 定义用户信息
        String userName = "CN=www.wangming.xyz, OU=IT, O=盐城市国有资产投资集团有限公司, L=Yancheng, ST=Yancheng, C=CN";

        // 定义根证书和私钥的 URL
        String certUrl = "http://127.0.0.1:8000/local-plus/66c1f165146fbf2cdaf53f55.cer";
        String privateKeyUrl = "http://127.0.0.1:8000/local-plus/66c1f166146fbf2cdaf53f56.pem";

        // 签发证书并获取 MultipartFile 列表
        List<MultipartFile> files = issueCertificate(userName, certUrl, privateKeyUrl, 1L);

        // 打印生成的文件名称
        for (MultipartFile file : files) {
            System.out.println("生成的文件: " + file.getOriginalFilename());
        }
    }
}
