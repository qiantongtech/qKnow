/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.ai.transformer;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;

import java.util.*;

/**
 * 文档 问答分块 转换器
 *
 * @author fabian
 */
public class QuestionAnswerEnricher implements DocumentTransformer {

    private final ChatModel chatModel;
    private final String QATemplate;
//    private final String language;

    public QuestionAnswerEnricher(ChatModel chatModel,String language) {
        String QATemplate = """
                根据以下文段，生成以该内容为答案的问题。文段内容如下:
                {content}
                将你的回答以如下的 json 格式输出：{"question":"","answer":""}。
                """;
        if (Objects.equals(language,"English")){
            QATemplate = QATemplate.concat("回答的语言使用英语来进行回答。");
        }
        this.chatModel = chatModel;
        this.QATemplate = QATemplate;
    }

    /**
     * 根据分块内容来生成问题
     *
     * @param documents the function argument
     * @return the function result
     */
    @Override
    public List<Document> apply(List<Document> documents) {
        BeanOutputConverter<Question> beanOutputConverter = new BeanOutputConverter<>(Question.class);
        Map<String, Object> map = new HashMap<>();
        List<Document> result = new ArrayList<>(documents.size());
        for (Document document : documents) {
            map.put("content", document.getText());
            String text = chatModel.call(StrUtil.format(QATemplate, map));
            Question question;
            try {
                question = beanOutputConverter.convert(text);
            } catch (Exception e) {
                continue;
            }

            if (StrUtil.isBlank(question.getQuestion())) {
                continue;
            }
            document.getMetadata().put("answer", question.getAnswer());

            Document newDoc = new Document(document.getId(), question.getQuestion(), document.getMetadata());
            result.add(newDoc);
        }

        return result;
    }

    /**
     * 将大模型的抽取出来的问题和回答都转成该对象进行处理
     */
    @Data
    private static class Question {
        private String question;
        private String answer;
    }
}
