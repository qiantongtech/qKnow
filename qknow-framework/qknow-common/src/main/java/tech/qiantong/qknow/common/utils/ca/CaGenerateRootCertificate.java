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

import cn.hutool.crypto.SecureUtil;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 生成自签名CA根证书并保存私钥为PEM文件
 * @author qknow
 */
public class CaGenerateRootCertificate {

    /**
     * 根据证书信息生成主体根证书
     * @param dnNameStr 证书信息
     * @return List<MultipartFile> 第一个是证书，第二个是私钥
     */
    public static List<MultipartFile> generateRootCertificate(String dnNameStr) {
        List<MultipartFile> files = new ArrayList<>();
        try {
            // 生成RSA密钥对
            KeyPair keyPair = SecureUtil.generateKeyPair("RSA", 2048);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 设定证书的有效期
            long currentTime = System.currentTimeMillis();
            Date startDate = new Date(currentTime);
            // 有效期30年
            Date endDate = new Date(currentTime + 365L * 30L * 24L * 60L * 60L * 1000L);

            // 设定证书信息
            X500Principal dnName = new X500Principal(dnNameStr);
            X500Name x500Name = new X500Name(dnName.getName());

            // 使用当前时间作为序列号
            BigInteger certSerialNumber = new BigInteger(Long.toString(currentTime));

            // 创建 X.509 证书构建器
            X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                    x500Name,                        // issuer
                    certSerialNumber,               // serial number
                    startDate,                      // not before
                    endDate,                        // not after
                    x500Name,                       // subject
                    publicKey                       // public key
            );

            // 创建内容签名器
            ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA")
                    .build(privateKey);

            // 生成证书
            X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);

            // 转换为标准 X509Certificate
            JcaX509CertificateConverter converter = new JcaX509CertificateConverter();
            java.security.cert.X509Certificate certificate = converter.getCertificate(certificateHolder);

            // 保存证书为 .cer 文件
            String certFilePath = "rootCA.cer";
            ByteArrayOutputStream certOutputStream = new ByteArrayOutputStream();
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(certOutputStream, StandardCharsets.US_ASCII))) {
                writer.write("-----BEGIN CERTIFICATE-----\n");
                writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(certificate.getEncoded()));
                writer.write("\n-----END CERTIFICATE-----\n");
            }
            Files.write(Paths.get(certFilePath), certOutputStream.toByteArray());

            // 保存私钥为 PEM 文件
            String privateKeyFilePath = "privateKey.pem";
            ByteArrayOutputStream pemOutputStream = new ByteArrayOutputStream();
            try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(pemOutputStream, StandardCharsets.US_ASCII))) {
                PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
                pemWriter.writeObject(pemObject);
            }

            // 将生成的文件转换为 MultipartFile
            files.add(convertFileToMultipartFile(certFilePath, "rootCA.cer"));
            files.add(new MockMultipartFile(privateKeyFilePath, privateKeyFilePath, "application/x-pem-file", pemOutputStream.toByteArray()));

            // 删除原始文件
            deleteFile(certFilePath);
            deleteFile(privateKeyFilePath);

            System.out.println("根证书和私钥已生成、转换为 MultipartFile 对象并删除原始文件");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

    private static MultipartFile convertFileToMultipartFile(String filePath, String fileName) throws Exception {
        File file = new File(filePath);
        try (FileInputStream input = new FileInputStream(file)) {
            return new MockMultipartFile(fileName, fileName, "application/x-x509-ca-cert", input);
        }
    }

    private static void deleteFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
            System.out.println("文件已删除: " + filePath);
        } catch (Exception e) {
            System.out.println("文件删除失败: " + filePath);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 测试生成自签名证书
        String dnNameStr = "CN=YcgtRootCA, OU=IT, O=盐城市国有资产投资集团有限公司, L=Yancheng, ST=Yancheng, C=CN";
        List<MultipartFile> files = generateRootCertificate(dnNameStr);

        // 打印生成的文件名称
        for (MultipartFile file : files) {
            System.out.println("生成的文件: " + file.getOriginalFilename());
        }
    }
}
