<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qKnow Knowledge Platform (Business Edition)
  Software Copyright Registration No. 15980140
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
-->

<template>
  <div class="app-container">
      <el-form
        ref="knowledgeBaseRef"
        :model="form"
        :rules="rules"
        label-width="auto"
        @submit.prevent
        v-if="!loading"
      >
        <!-- 1. 检索方式区块 -->
        <div class="section-block">
          <div class="header-text">
            <div class="header-left">
              <div class="blue-bar"></div>
              检索方式
            </div>
          </div>
          <div class="search-method-group">
            <!-- 向量检索 -->
            <div
              class="search-method-row"
              v-if="form.indexingTechnique !== 'economy'"
            >
              <div
                class="search-method-item"
                @click="form.searchMethod = 'semantic_search'"
                v-show="form.indexingTechnique !== 'economy'"
                style="display: inline-flex"
              >
                <el-radio
                  v-model="form.searchMethod"
                  label="semantic_search"
                  class="radio-label-hidden"
                />
                <div class="search-title">
                  <span>向量检索</span>
                </div>
              </div>
              <div
                v-show="form.indexingTechnique !== 'economy'"
                class="search-desc"
              >
                <el-icon class="desc-icon">
                  <WarningFilled />
                </el-icon>
                通过生成查询嵌入并查询与其向量表示最相似的文本分段。
              </div>
            </div>
            <div
              class="search-method-row"
              v-if="form.indexingTechnique !== 'economy'"
            >
              <!-- 全文检索 -->
              <div
                class="search-method-item"
                @click="form.searchMethod = 'full_text_search'"
                v-show="form.indexingTechnique !== 'economy'"
              >
                <el-radio
                  v-model="form.searchMethod"
                  label="full_text_search"
                  class="radio-label-hidden"
                />
                <div class="search-title">
                  <span>全文检索</span>
                </div>
              </div>
              <div
                v-show="form.indexingTechnique !== 'economy'"
                class="search-desc"
              >
                <el-icon class="desc-icon">
                  <WarningFilled />
                </el-icon>
                索引文档中的所有词汇，从而允许用户查询任意词汇，并返回包含这些词汇的文本片段。
              </div>
            </div>

            <div
              class="search-method-row"
              v-if="form.indexingTechnique !== 'economy'"
            >
              <!-- 混合检索 -->
              <div
                class="search-method-item"
                @click="form.searchMethod = 'hybrid_search'"
                v-show="form.indexingTechnique !== 'economy'"
              >
                <el-radio
                  v-model="form.searchMethod"
                  label="hybrid_search"
                  class="radio-label-hidden"
                />
                <div class="search-title">
                  <div class="search-recommend">
                    <span>混合检索</span>
                    <!--                    <el-tag size="small" type="primary" class="recommend-tag">推荐</el-tag>-->
                  </div>
                </div>
              </div>
              <div
                v-show="form.indexingTechnique !== 'economy'"
                class="search-desc"
              >
                <el-icon class="desc-icon">
                  <WarningFilled />
                </el-icon>
                同时执行全文检案和向量检索，并应用重排序步骤，从查询结果中选择匹配用户问题的最佳结果，用户可以选择设置权重或配置重新排序模型。
              </div>
            </div>

            <div class="search-method-row">
              <!-- 经济模式-倒排索引 -->
              <div
                class="search-method-item"
                @click="form.searchMethod = 'full_text_search'"
                v-show="form.indexingTechnique == 'economy'"
              >
                <el-radio
                  v-model="form.searchMethod"
                  class="radio-label-hidden"
                  label="full_text_search"
                />
                <div class="search-title">
                  <div class="search-recommend">
                    <span>倒排索引</span>
                  </div>
                </div>
              </div>
              <div
                v-show="form.indexingTechnique == 'economy'"
                class="search-desc"
              >
                <el-icon class="desc-icon">
                  <WarningFilled />
                </el-icon>
                通过生成查询嵌入并查询与其向量表示最相似的文本分段。
              </div>
            </div>
          </div>
        </div>

        <!-- 2. 检索规则区块 -->
        <div
          class="section-block"
          v-if="form.indexingTechnique !== 'economy'"
        >
          <div class="header-text">
            <div class="header-left">
              <div class="blue-bar"></div>
              检索规则
            </div>
          </div>
          <div
            class="search-rule-card"
            :class="{
              'no-border': form.searchMethod === 'hybrid_search',
              'hybrid-full':
                form.searchMethod == 'semantic_search' ||
                form.searchMethod === 'full_text_search',
            }"
          >
            <!-- 向量检索规则 -->
            <div
              v-show="form.searchMethod === 'semantic_search'"
              class="query-rule"
            >
              <el-form-item class="form-item-inline">
                <div class="option-label">Rerank模型启用状态:</div>
                <el-switch v-model="vectorData.rerankingEnable" />
              </el-form-item>
              <el-form-item class="form-item-inline">
                <div class="option-label">Rerank模型:</div>
                <el-select
                  v-model="vectorData.rerankingModelName"
                  placeholder="请选择Rerank模型"
                  :disabled="!vectorData.rerankingEnable"
                  class="select-short"
                >
                  <el-option-group
                    v-for="group in rerankingModel"
                    :key="group.label.zh_Hans"
                    :label="group.label.zh_Hans"
                  >
                    <el-option
                      v-for="item in group.models"
                      :key="item.model"
                      :label="item.model"
                      :value="item.model"
                    />
                  </el-option-group>
                </el-select>
              </el-form-item>

              <el-form-item class="form-item-inline">
                <div class="score-tran">
                  <div class="option-label">Score阈值启用状态:</div>
                  <el-switch
                    v-model="vectorData.scoreThresholdEnabled"
                    class="switch-margin"
                  />
                </div>
              </el-form-item>

              <el-form-item class="form-item-inline" prop="topK">
                <div class="label-input-icon">
                  <div class="label-input">
                    <div class="option-label">Top K:</div>
                    <el-input
                      v-model="vectorData.topK"
                      class="select-short"
                      type="number"
                      :min="1"
                      :max="10"
                      placeholder="请输入1-10的数字"
                    />
                  </div>
                  <div class="icon-text">
                    <el-icon class="desc-icon">
                      <WarningFilled />
                    </el-icon>
                    选择返回的相似文本数量
                  </div>
                </div>
              </el-form-item>

              <el-form-item
                class="form-item-inline score-rule-posi"
                prop="scoreThreshold"
              >
                <div class="label-input-icon">
                  <div class="label-input">
                    <div class="option-label score-offset">Score阈值:</div>
                    <el-input
                      v-model="vectorData.scoreThreshold"
                      :min="0"
                      :max="1"
                      :step="0.1"
                      type="number"
                      class="select-short"
                      placeholder="请输入0-1的小数"
                      :disabled="!vectorData.scoreThresholdEnabled"
                    />
                  </div>
                  <div class="icon-text icon-margin">
                    <el-icon class="desc-icon">
                      <WarningFilled />
                    </el-icon>
                    设置相似度分数的最小阈值
                  </div>
                </div>
              </el-form-item>
            </div>

            <!-- 全文检索规则 -->
            <div
              v-show="form.searchMethod === 'full_text_search'"
              class="query-rule"
            >
              <el-form-item class="form-item-inline">
                <div class="option-label">Rerank模型启用状态:</div>
                <el-switch v-model="fullTextData.rerankingEnable" />
              </el-form-item>
              <el-form-item class="form-item-inline">
                <div class="option-label">Rerank模型:</div>
                <el-select
                  v-model="fullTextData.rerankingModelName"
                  placeholder="请选择Rerank模型"
                  :disabled="!fullTextData.rerankingEnable"
                  class="select-short"
                >
                  <el-option-group
                    v-for="group in rerankingModel"
                    :key="group.label.zh_Hans"
                    :label="group.label.zh_Hans"
                  >
                    <el-option
                      v-for="item in group.models"
                      :key="item.model"
                      :label="item.model"
                      :value="item.model"
                    />
                  </el-option-group>
                </el-select>
              </el-form-item>
              <el-form-item class="form-item-inline">
                <div class="score-tran">
                  <div class="option-label">Score阈值启用状态:</div>
                  <el-switch
                    v-model="fullTextData.scoreThresholdEnabled"
                    class="switch-margin"
                  />
                </div>
              </el-form-item>
              <el-form-item class="form-item-inline" prop="topK">
                <div class="label-input-icon">
                  <div class="label-input">
                    <div class="option-label">Top K:</div>
                    <el-input
                      v-model="fullTextData.topK"
                      :min="1"
                      :max="10"
                      class="select-short"
                      placeholder="请输入1-10的数字"
                    />
                  </div>
                  <div class="icon-text">
                    <el-icon class="desc-icon">
                      <WarningFilled />
                    </el-icon>
                    选择返回的相似文本数量
                  </div>
                </div>
              </el-form-item>

              <el-form-item
                class="form-item-inline score-rule-posi"
                prop="scoreThreshold"
              >
                <div class="label-input-icon">
                  <div class="label-input">
                    <div class="option-label score-offset">Score阈值:</div>
                    <el-input
                      v-model="fullTextData.scoreThreshold"
                      :min="0"
                      :max="1"
                      :step="0.1"
                      class="select-short"
                      placeholder="请输入0-1的小数"
                      type="number"
                      :disabled="!fullTextData.scoreThresholdEnabled"
                    />
                  </div>
                  <div class="icon-text icon-margin">
                    <el-icon class="desc-icon">
                      <WarningFilled />
                    </el-icon>
                    设置相似度分数的最小阈值
                  </div>
                </div>
              </el-form-item>
            </div>

            <!-- 混合检索规则 -->
            <div v-show="form.searchMethod === 'hybrid_search'">
              <el-form-item class="form-item-inline">
                <div class="quan-rerank">
                  <div
                    @click="mixData.rerankingMode = 'weighted_score'"
                    class="label-input-query"
                  >
                    <div class="search-method-row-mix">
                      <div class="label-input">
                        <el-radio
                          v-model="mixData.rerankingMode"
                          label="weighted_score"
                          class="radio-label-hidden"
                        ></el-radio>
                        <div class="mode-title">权重设置</div>
                      </div>

                      <div class="icon-text mix-desc">
                        <el-icon class="desc-icon">
                          <WarningFilled />
                        </el-icon>
                        通过调整分配的权重，重新排序策略确定是优先进行语义匹配还是关键字匹配。
                      </div>
                    </div>

                    <div class="query-rule-mix">
                      <el-form-item
                        class="form-item-inline"
                        :prop="
                          mixData.rerankingMode === 'weighted_score'
                            ? 'weightAllocation'
                            : ''
                        "
                      >
                        <div class="option-label">权重分配:</div>
                        <el-input
                          v-model="mixData.keywordWeight"
                          :min="0"
                          :max="1.0"
                          :step="0.1"
                          class="select-short"
                          type="number"
                          :disabled="
                            mixData.rerankingMode === 'reranking_model'
                          "
                        ></el-input>
                      </el-form-item>

                      <el-form-item class="form-item-inline">
                        <div class="option-label">Score阈值启用状态:</div>
                        <el-switch
                          v-model="mixData.weightedScoreThresholdEnabled"
                          class="switch-margin"
                          :disabled="
                            mixData.rerankingMode === 'reranking_model'
                          "
                        />
                      </el-form-item>

                      <!-- 权重设置专属Top K -->
                      <el-form-item
                        class="form-item-inline topk-margin-r"
                        :prop="
                          mixData.rerankingMode === 'weighted_score'
                            ? 'topK'
                            : ''
                        "
                      >
                        <div class="label-input-icon">
                          <div class="label-input">
                            <div class="option-label">Top K:</div>
                            <el-input
                              v-model="mixData.weightedTopK"
                              :min="1"
                              :max="10"
                              controls-position="right"
                              class="select-short"
                              :disabled="
                                mixData.rerankingMode === 'reranking_model'
                              "
                            />
                          </div>

                          <div class="icon-text mix-tip">
                            <el-icon class="desc-icon">
                              <WarningFilled />
                            </el-icon>
                            选择返回的相似文本数量
                          </div>
                        </div>
                      </el-form-item>

                      <el-form-item
                        class="form-item-inline score-margin-wrap score-rule-posi-mix"
                        :prop="
                          mixData.rerankingMode === 'weighted_score'
                            ? 'scoreThreshold'
                            : ''
                        "
                      >
                        <div class="label-input-icon">
                          <div class="label-input">
                            <div class="option-label score-margin">
                              Score阈值:
                            </div>
                            <el-input
                              v-model="mixData.weightedScoreThreshold"
                              :min="0"
                              :max="1"
                              :step="0.1"
                              controls-position="right"
                              class="select-short"
                              placeholder="请输入0-1的小数"
                              type="number"
                              :disabled="
                                !mixData.weightedScoreThresholdEnabled ||
                                mixData.rerankingMode === 'reranking_model'
                              "
                            />
                          </div>

                          <div class="icon-text mix-score-tip">
                            <el-icon class="desc-icon">
                              <WarningFilled />
                            </el-icon>
                            设置相似度分数的最小阈值
                          </div>
                        </div>
                      </el-form-item>
                    </div>
                  </div>

                  <div
                    @click="mixData.rerankingMode = 'reranking_model'"
                    class="label-input-query"
                  >
                    <div class="search-method-row-mix">
                      <div class="label-input">
                        <el-radio
                          v-model="mixData.rerankingMode"
                          label="reranking_model"
                          class="radio-label-hidden"
                        ></el-radio>
                        <div class="mode-title">Rerank模型</div>
                      </div>

                      <div class="icon-text mix-desc">
                        <el-icon class="desc-icon">
                          <WarningFilled />
                        </el-icon>
                        重排序模型将根据候选文档列表与用户问题语义匹配度进行重新排序。
                      </div>
                    </div>

                    <div class="query-rule-mix">
                      <el-form-item class="form-item-inline">
                        <div class="option-label">Rerank模型:</div>
                        <el-select
                          v-model="mixData.rerankingModelName"
                          placeholder="请选择模型"
                          :disabled="
                            mixData.rerankingMode === 'weighted_score' ||
                            !mixData.rerankingEnable
                          "
                          class="select-short"
                        >
                          <el-option-group
                            v-for="group in rerankingModel"
                            :key="group.label.zh_Hans"
                            :label="group.label.zh_Hans"
                          >
                            <el-option
                              v-for="item in group.models"
                              :key="item.model"
                              :label="item.model"
                              :value="item.model"
                            />
                          </el-option-group>
                        </el-select>
                      </el-form-item>

                      <el-form-item class="form-item-inline">
                        <div class="option-label">Score阈值启用状态:</div>
                        <el-switch
                          v-model="mixData.rerankScoreThresholdEnabled"
                          class="switch-margin"
                          :disabled="mixData.rerankingMode === 'weighted_score'"
                        />
                      </el-form-item>
                      <el-form-item
                        class="form-item-inline topk-margin"
                        :prop="
                          mixData.rerankingMode === 'reranking_model'
                            ? 'topK'
                            : ''
                        "
                      >
                        <div class="label-input-icon">
                          <div class="label-input">
                            <div class="option-label">Top K:</div>
                            <el-input
                              v-model="mixData.rerankTopK"
                              :min="1"
                              :max="10"
                              controls-position="right"
                              class="select-short"
                              :disabled="
                                mixData.rerankingMode === 'weighted_score'
                              "
                            />
                          </div>

                          <div class="icon-text mix-tip">
                            <el-icon class="desc-icon">
                              <WarningFilled />
                            </el-icon>
                            选择返回的相似文本数量
                          </div>
                        </div>
                      </el-form-item>

                      <el-form-item
                        class="form-item-inline score-margin-wrap score-rule-posi-mix"
                        :prop="
                          mixData.rerankingMode === 'reranking_model'
                            ? 'scoreThreshold'
                            : ''
                        "
                      >
                        <div class="label-input-icon">
                          <div class="label-input">
                            <div class="option-label score-margin">
                              Score阈值:
                            </div>
                            <el-input
                              v-model="mixData.rerankScoreThreshold"
                              :min="0"
                              :max="1"
                              :step="0.1"
                              controls-position="right"
                              class="select-short"
                              placeholder="请输入0-1的小数"
                              type="number"
                              :disabled="
                                !mixData.rerankScoreThresholdEnabled ||
                                mixData.rerankingMode === 'weighted_score'
                              "
                            />
                          </div>

                          <div class="icon-text mix-score-tip">
                            <el-icon class="desc-icon">
                              <WarningFilled />
                            </el-icon>
                            设置相似度分数的最小阈值
                          </div>
                        </div>
                      </el-form-item>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </div>
          </div>
          <div class="underline"></div>
          <!-- 底部操作按钮 -->
          <div class="dialog-footer">
            <el-button type="default" size="small" plain @click="back">
              取消
            </el-button>
            <el-button type="primary" @click="submitForm"> 保存 </el-button>
          </div>
        </div>

        <!-- 经济模式规则 -->
        <div class="section-block" v-if="form.indexingTechnique == 'economy'">
          <div class="header-text">
            <div class="header-left">
              <div class="blue-bar"></div>
              检索规则
            </div>
          </div>
          <div class="search-rule-card economy-card" style="padding-top: 20px">
            <el-form-item class="form-item-inline" prop="topK">
              <div class="label-input-icon">
                <div class="label-input">
                  <div class="option-label">Top K:</div>
                  <el-input
                    v-model="form.topK"
                    :min="1"
                    :max="10"
                    controls-position="right"
                    class="select-short"
                  />
                </div>
                <div class="icon-text">
                  <el-icon class="desc-icon">
                    <WarningFilled />
                  </el-icon>
                  选择返回的相似文本数量
                </div>
              </div>
            </el-form-item>
          </div>
          <div class="underline"></div>
          <!-- 底部操作按钮 -->
          <div class="dialog-footer">
            <el-button type="default" size="small" plain @click="back">
              取消
            </el-button>
            <el-button type="primary" @click="submitForm"> 保存 </el-button>
          </div>
        </div>
      </el-form>
  </div>
</template>

<script setup>
import {
  addKnowledgeBase,
  getRerank,
  getTextEmbedding,
  updateKnowledgeBase,
  delKnowledgeBase,
  getKnowledgeBase,
  getRole,
  updateKnowledgeBaseRole,
  changeKnowledgeValid,
} from "@/api/kmc/knowledgeBase/knowledgeBase.js";
import { WarningFilled } from "@element-plus/icons-vue"; // 修正导入
import {
  reactive,
  ref,
  watch,
  watchEffect,
  nextTick,
  getCurrentInstance,
} from "vue";
import { useRoute, useRouter } from "vue-router";

const { proxy } = getCurrentInstance();
const { kmc_know_valid } = proxy.useDict("kmc_know_valid");

const data = reactive({
  form: {
    items: { row: [] }, // 标签数组容器
  },
  rules: {
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    validFlag: [{ required: true, message: "状态不能为空", trigger: "blur" }],
    indexingTechnique: [
      { required: true, message: "索引方式不能为空", trigger: "blur" },
    ],
    searchMethod: [
      { required: true, message: "检索方式不能为空", trigger: "blur" },
    ],
    embeddingModel: [
      { required: true, message: "请选择一个模型", trigger: "change" },
    ],
    // Score阈值通用校验规则
    scoreThreshold: [
      {
        required: true,
        message: "Score阈值不能为空",
        trigger: "blur",
        validator: (rule, value, callback) => {
          let enabled = false;
          let currentScoreThreshold = null;
          // 混合检索：判断当前模式的启用状态
          if (form.value.searchMethod === "hybrid_search") {
            if (mixData.value.rerankingMode === "weighted_score") {
              enabled = mixData.value.weightedScoreThresholdEnabled;
              currentScoreThreshold = mixData.value.weightedScoreThreshold;
            } else {
              enabled = mixData.value.rerankScoreThresholdEnabled;
              currentScoreThreshold = mixData.value.rerankScoreThreshold;
            }
          } else if (form.value.searchMethod === "semantic_search") {
            enabled = vectorData.value.scoreThresholdEnabled;
            currentScoreThreshold = vectorData.value.scoreThreshold;
          } else if (form.value.searchMethod === "full_text_search") {
            enabled = fullTextData.value.scoreThresholdEnabled;
            currentScoreThreshold = fullTextData.value.scoreThreshold;
          }
          // 启用时校验必填
          if (
            enabled &&
            !currentScoreThreshold &&
            currentScoreThreshold !== 0
          ) {
            callback(new Error("Score阈值不能为空"));
          } else {
            callback();
          }
        },
      },
      {
        validator: (rule, value, callback) => {
          let currentScoreThreshold = null;
          if (form.value.searchMethod === "hybrid_search") {
            currentScoreThreshold =
              mixData.value.rerankingMode === "weighted_score"
                ? mixData.value.weightedScoreThreshold
                : mixData.value.rerankScoreThreshold;
          } else if (form.value.searchMethod === "semantic_search") {
            currentScoreThreshold = vectorData.value.scoreThreshold;
          } else if (form.value.searchMethod === "full_text_search") {
            currentScoreThreshold = fullTextData.value.scoreThreshold;
          }
          // 空值且未启用时跳过
          if (!currentScoreThreshold && currentScoreThreshold !== 0) {
            callback();
            return;
          }
          // 限制 0 ≤ 值 ≤ 1
          if (currentScoreThreshold < 0 || currentScoreThreshold > 1) {
            callback(new Error("Score阈值必须在 0 到 1 之间"));
          } else {
            const decimalLen = (
              currentScoreThreshold.toString().split(".")[1] || ""
            ).length;
            if (decimalLen > 1) {
              callback(new Error("Score阈值最多保留 1 位小数"));
            } else {
              callback();
            }
          }
        },
        trigger: ["blur", "change"],
      },
    ],
    topK: [
      {
        validator: (rule, value, callback) => {
          let currentTopK = null;
          // 1. 根据业务逻辑获取实际需要校验的 Top K 值
          if (form.value.indexingTechnique === "economy") {
            currentTopK = form.value.topK;
          } else if (form.value.searchMethod === "hybrid_search") {
            currentTopK =
              mixData.value.rerankingMode === "weighted_score"
                ? mixData.value.weightedTopK
                : mixData.value.rerankTopK;
          } else if (form.value.searchMethod === "semantic_search") {
            currentTopK = vectorData.value.topK;
          } else if (form.value.searchMethod === "full_text_search") {
            currentTopK = fullTextData.value.topK;
          }

          // 2. 先校验非空（针对实际的 currentTopK）
          if (
            currentTopK === null ||
            currentTopK === undefined ||
            currentTopK === ""
          ) {
            callback(new Error("Top K不能为空"));
            return; // 非空校验失败，直接返回，不执行后续范围校验
          }

          // 3. 再校验数值范围
          const numTopK = Number(currentTopK); // 确保是数值类型
          if (isNaN(numTopK) || numTopK < 1 || numTopK > 10) {
            callback(new Error("Top K必须在 1 到 10 之间"));
          } else {
            callback(); // 校验通过，执行回调
          }
        },
        trigger: ["blur", "change"], // 统一触发时机，覆盖所有场景
      },
    ],
    weightAllocation: [
      {
        required: true,
        message: "权重分配不能为空",
        trigger: "blur",
        validator: (rule, value, callback) => {
          // 仅在「混合检索+权重模式」下校验必填
          if (
            form.value.searchMethod === "hybrid_search" &&
            mixData.value.rerankingMode === "weighted_score"
          ) {
            if (
              !mixData.value.keywordWeight &&
              mixData.value.keywordWeight !== 0
            ) {
              callback(new Error("权重分配不能为空"));
            } else {
              callback();
            }
          } else {
            callback(); // 非权重模式跳过必填校验
          }
        },
      },
      {
        validator: (rule, value, callback) => {
          const currentWeight = mixData.value.keywordWeight;
          // 空值且非权重模式时跳过校验（避免禁用状态报错）
          if (
            !currentWeight &&
            currentWeight !== 0 &&
            !(
              form.value.searchMethod === "hybrid_search" &&
              mixData.value.rerankingMode === "weighted_score"
            )
          ) {
            callback();
            return;
          }
          // 校验0-1范围
          if (currentWeight < 0 || currentWeight > 1) {
            callback(new Error("权重分配必须在 0 到 1 之间"));
          } else {
            // 校验最多1位小数
            const decimalLen = (currentWeight.toString().split(".")[1] || "")
              .length;
            if (decimalLen > 1) {
              callback(new Error("权重分配最多保留 1 位小数"));
            } else {
              callback();
            }
          }
        },
        trigger: ["blur", "change"],
      },
    ],
  },
});

const vectorData = ref({
  rerankingEnable: null,
  rerankingModelName: null,
  topK: 1,
  scoreThresholdEnabled: null,
  scoreThreshold: 0,
});
const fullTextData = ref({
  rerankingEnable: null,
  rerankingModelName: null,
  topK: 1,
  scoreThresholdEnabled: null,
  scoreThreshold: 0,
});
const mixData = ref({
  rerankingEnable: null,
  rerankingModelName: null,
  // 公共变量（最终提交用）
  topK: 1,
  scoreThreshold: 0,
  scoreThresholdEnabled: null,
  // 权重设置专属变量
  weightedTopK: 1,
  weightedScoreThreshold: 0,
  weightedScoreThresholdEnabled: null,
  // Rerank模型专属变量
  rerankTopK: 1,
  rerankScoreThreshold: 0,
  rerankScoreThresholdEnabled: null,
  keywordWeight: 0.5,
  vectorWeight: null,
  rerankingMode: "weighted_score",
});

// 标签操作
const handleClose = (tag) => {
  const index = form.value.items.row.findIndex((t) => t.name === tag.name);
  if (index !== -1) {
    form.value.items.row.splice(index, 1);
    form.value.tags = JSON.stringify(form.value.items.row);
  }
};

const handleInputConfirm = () => {
  const value = inputValue.value.trim();
  if (value) {
    form.value.items.row.push({ name: value });
    form.value.tags = JSON.stringify(form.value.items.row);
  }
  inputVisible.value = false;
  inputValue.value = "";
};

const showInput = () => {
  inputVisible.value = true;
  nextTick(() => {
    if (InputRef.value && InputRef.value.input) {
      InputRef.value.input.focus();
    }
  });
};

// 响应式变量
const isDisabled = ref(false);
const { form, rules } = toRefs(data);
const title = ref("");
const embeddingModel = ref([]);
const rerankingModel = ref([]);
const platForm = ref("local");
const route = useRoute();
const open = ref(false);
const roleLoading = ref(false);
const loading = ref(true); // 初始为加载中
const roleList = ref([]);
const roleIds = ref();
const roleTableRef = ref(null);
const router = useRouter();
const inputValue = ref("");
const inputVisible = ref(false);
const InputRef = ref(null);
let kbid;
const buttonShow = ref(false); // 控制删除按钮显示

// 监听路由参数变化
watch(
  () => route.params.kbId,
  (newKbId) => {
    kbid = newKbId;
    handleUpdate(newKbId);
    if (newKbId == undefined) {
      newKbId = 0;
    }
    handleDataScope(newKbId);
  },
  { deep: true, immediate: true }
);

// 模型联动逻辑
watchEffect(() => {
  const selectedModel = form.value.embeddingModel;
  if (!selectedModel) return;
  for (const group of embeddingModel.value) {
    const found = group.models.find((item) => item.model === selectedModel);
    if (found) {
      form.value.embeddingModelProvider = group.provider;
      return;
    }
  }
  form.value.embeddingModelProvider = "";
});

watchEffect(() => {
  const selectedModel = form.value.rerankingModelName;
  if (!selectedModel) return;
  for (const group of rerankingModel.value) {
    const found = group.models.find((item) => item.model === selectedModel);
    if (found) {
      form.value.rerankingProviderName = group.provider;
      return;
    }
  }
  form.value.rerankingProviderName = "";
});

// 权重计算
watch(
  () => mixData.value.keywordWeight,
  (newVal) => {
    mixData.value.vectorWeight = Math.round((1.0 - newVal) * 100) / 100;
  },
  { immediate: true }
);

// 表单重置
function reset() {
  form.value = {
    id: null,
    workspaceId: null,
    qmDatasetId: null,
    name: null,
    items: { row: [] },
    tags: null,
    description: null,
    indexingTechnique: "high_quality",
    permission: null,
    embeddingModel: null,
    embeddingModelProvider: null,
    searchMethod: "hybrid_search",
    rerankingEnable: false,
    rerankingProviderName: null,
    rerankingModelName: null,
    topK: 1,
    scoreThresholdEnabled: null,
    scoreThreshold: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
    keywordWeight: null,
    vectorWeight: null,
    rerankingMode: null,
  };
  proxy.resetForm("knowledgeBaseRef");
}

// 加载数据
function handleUpdate(_id) {
  reset();
  title.value = "添加知识库";
  if (_id) {
    buttonShow.value = true;
    getKnowledgeBase(_id).then((response) => {
      form.value = response.data;
      // 处理标签数据
      if (form.value.tags) {
        form.value.items = { row: JSON.parse(form.value.tags) };
      } else {
        form.value.items = { row: [] };
      }
      // 初始化检索参数
      if (form.value.searchMethod === "semantic_search") {
        Object.assign(vectorData.value, {
          rerankingEnable: form.value.rerankingEnable,
          rerankingModelName: form.value.rerankingModelName,
          topK: form.value.topK,
          scoreThresholdEnabled: form.value.scoreThresholdEnabled,
          scoreThreshold: form.value.scoreThreshold,
        });
      } else if (form.value.searchMethod === "full_text_search") {
        Object.assign(fullTextData.value, {
          rerankingEnable: form.value.rerankingEnable,
          rerankingModelName: form.value.rerankingModelName,
          topK: form.value.topK,
          scoreThresholdEnabled: form.value.scoreThresholdEnabled,
          scoreThreshold: form.value.scoreThreshold,
        });
      } else if (form.value.searchMethod === "hybrid_search") {
        // 混合检索回显：拆分到专属变量
        Object.assign(mixData.value, {
          rerankingEnable: form.value.rerankingEnable,
          rerankingModelName: form.value.rerankingModelName,
          keywordWeight: form.value.keywordWeight,
          vectorWeight: form.value.vectorWeight,
          rerankingMode: form.value.rerankingMode || "weighted_score",
          // 根据模式回显到专属变量
          weightedTopK:
            form.value.rerankingMode === "weighted_score" ? form.value.topK : 1,
          weightedScoreThreshold:
            form.value.rerankingMode === "weighted_score"
              ? form.value.scoreThreshold
              : 0,
          weightedScoreThresholdEnabled:
            form.value.rerankingMode === "weighted_score"
              ? form.value.scoreThresholdEnabled
              : null,
          rerankTopK:
            form.value.rerankingMode === "reranking_model"
              ? form.value.topK
              : 1,
          rerankScoreThreshold:
            form.value.rerankingMode === "reranking_model"
              ? form.value.scoreThreshold
              : 0,
          rerankScoreThresholdEnabled:
            form.value.rerankingMode === "reranking_model"
              ? form.value.scoreThresholdEnabled
              : null,
          // 公共变量暂存（不影响）
          topK: form.value.topK,
          scoreThreshold: form.value.scoreThreshold,
          scoreThresholdEnabled: form.value.scoreThresholdEnabled,
        });
      }
      title.value = "修改知识库";
      if (form.value.indexingTechnique === "high_quality") {
        isDisabled.value = true;
      }
    });
  }
}

// 提交表单
function submitForm() {
  if (form.value.indexingTechnique !== "economy") {
    // 同步检索参数到表单
    if (form.value.searchMethod === "semantic_search") {
      form.value.rerankingEnable = vectorData.value.rerankingEnable;
      form.value.rerankingModelName = vectorData.value.rerankingModelName;
      form.value.topK = vectorData.value.topK;
      form.value.scoreThresholdEnabled = vectorData.value.scoreThresholdEnabled;
      form.value.scoreThreshold = vectorData.value.scoreThreshold;
    } else if (form.value.searchMethod === "full_text_search") {
      form.value.rerankingEnable = fullTextData.value.rerankingEnable;
      form.value.rerankingModelName = fullTextData.value.rerankingModelName;
      form.value.topK = fullTextData.value.topK;
      form.value.scoreThresholdEnabled =
        fullTextData.value.scoreThresholdEnabled;
      form.value.scoreThreshold = fullTextData.value.scoreThreshold;
    } else if (form.value.searchMethod === "hybrid_search") {
      //混合检索：根据模式赋值专属变量到公共变量
      if (mixData.value.rerankingMode === "weighted_score") {
        // 权重设置模式：取权重侧的变量
        mixData.value.topK = mixData.value.weightedTopK;
        mixData.value.scoreThreshold = mixData.value.weightedScoreThreshold;
        mixData.value.scoreThresholdEnabled =
          mixData.value.weightedScoreThresholdEnabled;
      } else {
        // Rerank模型模式：取模型侧的变量
        mixData.value.topK = mixData.value.rerankTopK;
        mixData.value.scoreThreshold = mixData.value.rerankScoreThreshold;
        mixData.value.scoreThresholdEnabled =
          mixData.value.rerankScoreThresholdEnabled;
      }

      // 原有混合检索同步逻辑（现在取的是赋值后的公共变量）
      form.value.rerankingEnable = mixData.value.rerankingEnable;
      form.value.rerankingModelName = mixData.value.rerankingModelName;
      form.value.topK = mixData.value.topK;
      form.value.scoreThresholdEnabled = mixData.value.scoreThresholdEnabled;
      form.value.scoreThreshold = mixData.value.scoreThreshold;
      form.value.keywordWeight = mixData.value.keywordWeight;
      form.value.vectorWeight = mixData.value.vectorWeight;
      form.value.rerankingMode = mixData.value.rerankingMode;
    }
  }

  // 表单验证与提交
  proxy.$refs["knowledgeBaseRef"].validate((valid) => {
    if (valid) {
      // 区分修改和新增场景
      if (form.value.id) {
        // 修改场景：已有ID，先更新基础信息，再更新权限
        updateKnowledgeBase(form.value).then(() => {
          updateKnowledgeBaseRole({
            knowledgeId: form.value.id,
            roleIds: roleIds.value,
          }).then(() => {
            proxy.$modal.msgSuccess("保存成功");
          });
        });
      } else {
        // 新增场景：先新增知识库，获取ID后再绑定权限
        addKnowledgeBase(form.value).then((response) => {
          const newKnowledgeId = response.data;
          if (newKnowledgeId) {
            updateKnowledgeBaseRole({
              knowledgeId: newKnowledgeId,
              roleIds: roleIds.value,
            }).then(() => {
              proxy.$modal.msgSuccess("新增成功");
              const obj = { path: "/kmc/knowledgeBase" };
              proxy.$tab.closeOpenPage(obj).then(() => {
                proxy.$tab.refreshPage();
              });
            });
          } else {
            proxy.$modal.msgError("新增失败，未获取到知识库ID");
          }
        });
      }
    }
  });
}

// 权限相关
function handleDataScope(id) {
  roleLoading.value = true;
  getRole(id).then((response) => {
    roleLoading.value = false;
    roleList.value = response.data.roleList;
    const selectedRoleIds =
      id === 0
        ? []
        : response.data.knowledgeRoleList.map((item) => item.roleId);
    roleIds.value = selectedRoleIds.join(",");
    nextTick(() => {
      roleList.value.forEach((item) => {
        if (selectedRoleIds.includes(item.roleId)) {
          // roleTableRef.value.toggleRowSelection(item, true);
        } else {
          // roleTableRef.value.toggleRowSelection(item, false);
        }
      });
    });
  });
}

function handleSelectionChange(selection) {
  roleIds.value = selection.map((item) => item.roleId).join(",");
}

// 删除操作
function handleDelete() {
  const _ids = kbid;
  proxy.$modal
    .confirm(`是否确认删除知识库编号为"${_ids}"的数据项？`)
    .then(() => delKnowledgeBase(_ids))
    .then(() => {
      proxy.$modal.msgSuccess("删除成功");
      router.push({ path: "/kmc/knowledgeBase" });
    })
    .catch(() => {});
}

// 返回
function back() {
  const obj = { path: "/kmc/knowledgeBase" };
  proxy.$tab.closeOpenPage(obj);
}

// 初始化模型数据
function init() {
  getTextEmbedding().then((response) => {
    embeddingModel.value = response.data;
  });
  getRerank().then((response) => {
    rerankingModel.value = response.data;
    if (rerankingModel.value.length) {
      const defaultModel = rerankingModel.value[0].models[0].model;
      //只有值为空时，才赋值默认值（避免覆盖接口返回的已有值）
      if (!vectorData.value.rerankingModelName) {
        vectorData.value.rerankingModelName = defaultModel;
      }
      if (!fullTextData.value.rerankingModelName) {
        fullTextData.value.rerankingModelName = defaultModel;
      }
      if (!mixData.value.rerankingModelName) {
        // 重点：mixData 只在空时赋值
        mixData.value.rerankingModelName = defaultModel;
      }
      // 兜底默认值（仅初始化，不覆盖已有值）
      if (vectorData.value.rerankingEnable === null) {
        vectorData.value.rerankingEnable = false;
      }
      if (fullTextData.value.rerankingEnable === null) {
        fullTextData.value.rerankingEnable = false;
      }
      if (mixData.value.rerankingEnable === null) {
        mixData.value.rerankingEnable = true;
      }
    }
    loading.value = false; // 模型数据加载完成，关闭加载
  });
}

// 初始化
init();
</script>

<style scoped lang="scss">
// 基础通用样式
$primary-color: #2666fb;
$tip-bg: #fffdf0;
$tip-border: #ffe58f;
$tip-icon-color: #efbd47;
$text-color-main: #333333;
$text-color-tip: #888888;
$border-color: #e6e6e6;
$underline-color: #f4f4f4;

// 全局容器样式
.app-container {
  .pagecont-top {
    height: 100%;
    padding: 0;
    background-color: #f0f2f5;
  }
}

// 区块通用样式
.section-block {
  margin-bottom: 10px;
  background-color: #ffffff;
  padding: 20px;
  &:last-child {
    margin-bottom: 0;
  }

  .header-text {
    display: flex;
    align-items: center;
    margin: 10px 0;
    font-weight: 500;
    font-size: 14px;
    color: $text-color-main;

    .header-left {
      display: flex;
      align-items: center;
      color: #666666;
      font-size: 16px;
      line-height: 24px;
      font-family: PingFangSC-Medium;

      .blue-bar {
        background-color: $primary-color;
        width: 6px;
        height: 16px;
        margin-right: 10px;
        border-radius: 3px;
      }
    }
  }
}

// 检索方式组样式
.search-method-group {
  display: flex;
  //flex-wrap: wrap;
  flex-direction: column;

  .search-method-row {
    @extend %search-method;
  }

  .search-method-item {
    display: flex;
    cursor: pointer;
    transition: all 0.2s;
    align-items: center;
    margin-left: 17px;

    .search-title {
      min-width: 56px;
      font-size: 14px;
      font-family: PingFang SC-Medium;
      color: rgba(0, 0, 0, 0.85);
      line-height: 22px;

      .search-recommend {
        display: flex;
        align-items: center;

        .recommend-tag {
          margin-left: 8px;
        }
      }
    }
  }

  // 检索方式描述（复用提示文本样式）
  .search-desc {
    @extend .base-tip-style;
    margin-left: 18px;
    //width: 532px;
  }
}

// 检索规则卡片通用样式
.search-rule-card {
  border: 1px solid $border-color;
  border-radius: 4px;
  padding: 0px 16px 15px 16px;

  // 向量/全文检索规则布局
  .query-rule {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    justify-items: start;
    padding-top: 20px;

    @media (min-width: 1280px) {
      grid-template-columns: 300px 1fr 200px;
    }
    @media (min-width: 1366px) {
      grid-template-columns: 350px 1fr 250px;
    }
    @media (min-width: 1440px) {
      grid-template-columns: 400px 1fr 300px;
    }
    @media (min-width: 1536px) {
      grid-template-columns: 450px 1fr 350px;
    }
    @media (min-width: 1712px) {
      grid-template-columns: 500px 1fr 380px;
    }
    @media (min-width: 2560px) {
      grid-template-columns: 580px 1fr 400px;
    }

    .score-tran {
      display: flex;
      align-items: center;
      transform: translateY(52px);
    }
  }

  // 混合检索规则布局
  .query-rule-mix {
    padding: 25px 20px 5px 20px;
    margin: 10px 0px 10px 0;
    border: 1px solid #ebebeb;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    justify-items: start;

    .topk-margin {
      margin-left: 37px;
    }

    .topk-margin-r {
      margin-left: 19px;
    }

    .score-margin {
      margin-left: 28px;
    }
  }

  // 表单行通用样式
  .form-item-inline {
    display: flex;
    align-items: center;
    margin-bottom: 20px;

    .option-label {
      margin-right: 12px;
      font-family: PingFangSC-Regular;
      font-size: 14px;
      line-height: 22px;
      color: $text-color-main;
    }

    // 输入框/选择器短样式
    .select-short {
      width: 372px;
      height: 32px;
      @media (max-width: 1260px) {
        width: 100px;
      }
      @media (min-width: 1260px) {
        width: 170px;
      }
      @media (min-width: 1366px) {
        width: 190px;
      }
      @media (min-width: 1440px) {
        width: 210px;
      }
      @media (min-width: 1536px) {
        width: 270px;
      }
      @media (min-width: 1712px) {
        width: 300px;
      }
      @media (min-width: 2560px) {
        width: 400px;
      }

      ::v-deep .el-select__wrapper {
        height: 100% !important;
        min-height: unset !important;
        align-items: center;
        border-radius: 3px !important;
      }
    }

    // 开关间距
    .switch-margin {
      margin-left: 8px;
    }

    // 标签+输入+图标文本容器
    .label-input-icon {
      display: flex;
      flex-direction: column;
      align-items: flex-start;

      .label-input {
        display: flex;
        align-items: center;
        margin-bottom: 8px;
      }

      // 提示文本（复用基础提示样式）
      .icon-text {
        @extend .base-tip-style;
        margin-left: 52px;
      }

      // Score阈值提示文本偏移
      .icon-margin {
        margin-left: 90px;
      }
    }

    // 混合检索-权重/Rerank模型容器
    .label-input-query {
      display: flex;
      flex-direction: column;
      width: 100%;

      .search-method-row-mix {
        @extend %search-method;
        //margin-top: 0px !important;
      }

      .label-input {
        display: flex;
        align-items: center;

        .mode-title {
          font-family: PingFang SC-Medium;
          font-size: 14px;
          line-height: 22px;
          color: rgba(0, 0, 0, 0.85);
          margin-right: 18px;
        }
      }

      // 混合检索描述文本
      .mix-desc {
        @extend .base-tip-style;
        //width: 552px;
        //background-color: #fffdf1;
        //border-color: #f5ecbe;
        //margin: 8px 0 16px 0;
      }

      // 混合检索TopK提示文本
      .mix-tip {
        @extend .base-tip-style;
        margin-left: 53px;
        margin-top: 0px;
      }

      // 混合检索Score阈值提示文本
      .mix-score-tip {
        @extend .base-tip-style;
        margin-left: 109px;
        margin-top: 0px;
      }
    }

    // 混合检索-权重/Rerank容器
    .quan-rerank {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 24px;
    }

    // Score阈值标签偏移
    .score-offset {
      margin-left: 9px;
    }

    // Score阈值输入行偏移
    .score-margin-wrap {
      margin-left: -28px;
    }
  }
}

// 混合检索去除边框
.search-rule-card.no-border {
  border: none !important;
}

// 经济模式卡片样式
.search-rule-card.economy-card {
  height: 110px !important;
}

// 下划线样式
.underline {
  border-bottom: 2px solid $underline-color;
  margin: 0 0 15px 0;
}

// 按钮区样式
.dialog-footer {
  display: flex;
  justify-content: flex-end;

  .el-button {
    height: 28px;
    width: 80px;
    font-size: 12px !important;
    margin-left: 8px;

    img {
      margin-right: 6px;
      width: 11px;
      height: 11px;
    }
  }
}

// 隐藏radio标签
.radio-label-hidden {
  margin-right: 15px;

  ::v-deep .el-radio__label {
    display: none !important;
  }
}

// 向量/全文检索下划线间距
.section-block:has(.search-rule-card.hybrid-full) .underline {
  margin-top: 261px !important;
}

// 经济模式下划线间距
.section-block:has(.search-rule-card.economy-card) .underline {
  margin-top: 425px !important;
}

// 基础提示文本样式（复用核心）
%base-tip-style {
  display: flex;
  align-items: center;
  font-size: 14px;
  line-height: 22px;
  font-family: Microsoft YaHei-Regular;
  //background-color: $tip-bg;
  //border: 1px solid $tip-border;
  color: $text-color-tip;

  .desc-icon {
    margin-right: 3px;
    //color: $tip-icon-color;
  }
}

%search-method {
  display: flex;
  align-items: center;
  margin-top: 13px;
}

.base-tip-style {
  @extend %base-tip-style;
}

:deep(.score-rule-posi .el-form-item__error) {
  margin-left: 10px !important; // 距离输入框底部的距离
}

:deep(.score-rule-posi-mix .el-form-item__error) {
  margin-left: 28px !important; // 距离输入框底部的距离
}
</style>
