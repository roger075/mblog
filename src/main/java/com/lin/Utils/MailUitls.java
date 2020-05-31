package com.lin.Utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUitls {

    public static void sendEmailForResetPwd(String email,int code){
        String from = "332554514@qq.com";
        String host = "smtp.qq.com";

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host",host);
        properties.setProperty("mail.smtp.auth","true");
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable","true");
            properties.put("mail.smtp.ssl.socketFactory",sf);

            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from,"vywndecduzwvbihd");
                    }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));

            message.setSubject("重置密码");

            String content = "<html><head></head><body><h1>【校园博客系统】</h1><h3>您正在使用修改密码功能，验证码为 "+code+" 。如果不是本人操作，请忽略该邮件，如果一直出现问题请联系 管理员 [请勿回复]</h3></body></html>";

            message.setContent(content,"text/html;charset=UTF-8");

            Transport.send(message);

            System.out.println("邮件发送成功");

        } catch (Exception e){
            e.printStackTrace();
        }



    }

}
