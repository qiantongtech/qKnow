package tech.qiantong.qknow.module.ext.service.deepke;

import tech.qiantong.qknow.common.core.domain.AjaxResult;
//import tech.qiantong.qknow.module.ext.api.extration.dto.ExtExtractionDTO;

/**
 * 知识抽取
 */
public interface DeepkeExtractionService {
    AjaxResult deepkeExtraction(String text);
}
