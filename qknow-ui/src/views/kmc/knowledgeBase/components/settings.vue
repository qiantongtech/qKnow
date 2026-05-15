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
    <div class="pagecont-top">
      <!-- 基础设置标题区 -->
      <div class="pagecont-top-title">
        <div class="header-text">
          <div class="header-left">
            <div class="blue-bar"></div>
            {{ title }}
          </div>
        </div>
        <!--        <el-button type="primary" size="small" plain @click="back">-->
        <!--          <img class="currImg" src="@/assets/kg/back.svg" alt="">-->
        <!--          <img class="act" src="@/assets/kg/back-act.svg" alt="">返回-->
        <!--        </el-button>-->
      </div>

      <!-- 基础设置表单区 -->
      <div class="form-div">
        <el-form
          ref="knowledgeBaseRef"
          :model="form"
          :rules="rules"
          label-width="auto"
          @submit.prevent
        >
          <el-row :gutter="20">
            <el-col :span="13">
              <el-form-item label="名称" prop="name" class="name-form-item">
                <el-input v-model="form.name" placeholder="请输入名称" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item
                label="封面图"
                prop="coverImage"
                class="image-form-item"
              >
                <image-upload
                  v-model="form.coverImage"
                  :limit="1"
                  :fileSize="10"
                  :isShowTip="true"
                  :platForm="platForm"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="18">
              <el-form-item label="标签" prop="tags" class="tag-form-item">
                <div class="flex gap-2">
                  <el-tag
                    v-for="(tag, index) in form.items.row"
                    :key="index"
                    closable
                    :disable-transitions="false"
                    @close="handleClose(tag)"
                  >
                    {{ tag.name }}
                  </el-tag>
                  <el-input
                    v-if="inputVisible"
                    ref="InputRef"
                    v-model="inputValue"
                    class="w-20"
                    size="small"
                    @keyup.enter="handleInputConfirm"
                    @blur="handleInputConfirm"
                  />
                  <el-button
                    v-else
                    class="button-new-tag"
                    size="small"
                    @click="showInput"
                  >
                    + 添加标签
                  </el-button>
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item
                class="status-form-item"
                label="状态"
                prop="validFlag"
                style="display: flex; align-items: center"
              >
                <el-radio-group v-model="form.validFlag">
                  <el-radio
                    v-for="item in kmc_know_valid"
                    :key="item.value"
                    :label="item.value === 'true'"
                  >
                    {{ item.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item
                label="描述"
                prop="description"
                class="desc-form-item"
              >
                <el-input
                  type="textarea"
                  v-model="form.description"
                  placeholder="请输入描述"
                  maxlength="256"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>

          <!--          <div class="underline"></div>-->

          <!-- 索引模式 -->
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item
                label="索引方式"
                prop="indexingTechnique"
                class="index-form-item"
              >
                <div class="indexing-container">
                  <div
                    :class="[
                      'indexing-item',
                      form.indexingTechnique === 'high_quality' ? 'act' : '',
                    ]"
                    @click="form.indexingTechnique = 'high_quality'"
                  >
                    <div class="indexing-title">
                      <el-icon class="icon"><Trophy /></el-icon>
                      <span>高质量</span>
                      <el-tag size="small" type="primary" class="recommend-tag"
                        >推荐</el-tag
                      >
                    </div>
                    <p class="indexing-desc">
                      调用高级模型来处理文本以实现更精确的检索，可以相对大语言模型生成高质量的回答。
                    </p>
                  </div>
                  <el-tooltip
                    class="item"
                    effect="dark"
                    content="无法从高质量降级为经济"
                    placement="top"
                    :disabled="!isDisabled"
                  >
                    <div
                      :class="[
                        'indexing-item',
                        form.indexingTechnique === 'economy' ? 'act' : '',
                        isDisabled ? 'disabled' : '',
                      ]"
                      @click="
                        isDisabled ? null : (form.indexingTechnique = 'economy')
                      "
                    >
                      <div class="indexing-title">
                        <el-icon class="icon">
                          <img
                            src="@/assets/kmc/Money.png"
                            alt=""
                            v-if="form.indexingTechnique !== 'economy'"
                          />
                          <img
                            src="@/assets/kmc/Money2.png"
                            alt=""
                            v-if="form.indexingTechnique == 'economy'"
                          />
                        </el-icon>
                        <span>经济</span>
                      </div>
                      <p class="indexing-desc">
                        每个块使用 10
                        个关键词进行检索，不消耗tokens，但会降低检索准确性。
                      </p>
                    </div>
                  </el-tooltip>
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <!-- Embedding 模型 -->
          <el-row :gutter="20">
            <el-col :span="13">
              <el-form-item
                label="Embedding模型"
                prop="embeddingModel"
                class="emdedding-form-item"
              >
                <el-select
                  v-model="form.embeddingModel"
                  placeholder="请选择Embedding模型"
                >
                  <el-option-group
                    v-for="group in embeddingModel"
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
            </el-col>
          </el-row>

          <div
            :class="[
              'underline-base',
              isAdd ? 'underline-add' : 'underline-edit',
            ]"
          ></div>

          <!-- 检索设置 -->
          <!--          <el-row :gutter="20">-->
          <!--            <el-col :span="24">-->
          <!--              <div class="header-text">-->
          <!--                <div class="header-left">-->
          <!--                  <div class="blue-bar"></div>-->
          <!--                  检索设置-->
          <!--                </div>-->
          <!--              </div>-->
          <!--              <el-form-item prop="searchMethod">-->
          <!--                <div class="search-container">-->
          <!--                  &lt;!&ndash; 向量检索 &ndash;&gt;-->
          <!--                  <div-->
          <!--                      class="search-item"-->
          <!--                      :class="{ act: form.searchMethod === 'semantic_search' }"-->
          <!--                      @click="form.searchMethod = 'semantic_search'"-->
          <!--                      v-if="form.indexingTechnique !== 'economy'"-->
          <!--                  >-->
          <!--                    <div class="search-title">-->
          <!--                      <el-icon class="icon">-->
          <!--                        <img src="@/assets/kmc/Search.png" alt="" v-if="form.searchMethod !== 'semantic_search'"/>-->
          <!--                        <img src="@/assets/kmc/Search2.png" alt="" v-if="form.searchMethod == 'semantic_search'"/>-->
          <!--                      </el-icon>-->
          <!--                      <span>向量检索</span>-->
          <!--                    </div>-->
          <!--                    <div class="search-desc">-->
          <!--                      通过生成查询嵌入并查询与其向量表示最相似的文本分段-->
          <!--                    </div>-->
          <!--                    <div class="search-options">-->
          <!--                      <el-form-item class="form-item-inline" prop="rerankingEnable">-->
          <!--                        <div class="option-label">Rerank 模型</div>-->
          <!--                        <el-switch v-model="vectorData.rerankingEnable"/>-->
          <!--                      </el-form-item>-->
          <!--                      <el-form-item class="form-item-full" prop="rerankingModelName">-->
          <!--                        <el-select-->
          <!--                            v-model="vectorData.rerankingModelName"-->
          <!--                            placeholder="请选择模型"-->
          <!--                            :disabled="!vectorData.rerankingEnable"-->
          <!--                        >-->
          <!--                          <el-option-group-->
          <!--                              v-for="group in rerankingModel"-->
          <!--                              :key="group.label.zh_Hans"-->
          <!--                              :label="group.label.zh_Hans"-->
          <!--                          >-->
          <!--                            <el-option-->
          <!--                                v-for="item in group.models"-->
          <!--                                :key="item.model"-->
          <!--                                :label="item.model"-->
          <!--                                :value="item.model"-->
          <!--                            />-->
          <!--                          </el-option-group>-->
          <!--                        </el-select>-->
          <!--                      </el-form-item>-->
          <!--                      <div class="search-config">-->
          <!--                        <div class="config-item">-->
          <!--                          <div class="config-label">-->
          <!--                            <span>Top K</span>-->
          <!--                            <el-tooltip content="选择返回的相似文本数量">-->
          <!--                              <img class="icon-question" src="@/assets/kmc/QuestionFilled.png" alt="" />-->
          <!--                            </el-tooltip>-->
          <!--                          </div>-->
          <!--                          <div class="config-content">-->
          <!--                            <el-input-number-->
          <!--                                v-model="vectorData.topK"-->
          <!--                                :min="1"-->
          <!--                                :max="10"-->
          <!--                                controls-position="right"-->
          <!--                                class="input-number"-->
          <!--                            />-->
          <!--                            <el-slider-->
          <!--                                v-model="vectorData.topK"-->
          <!--                                :min="1"-->
          <!--                                :max="10"-->
          <!--                                class="slider"-->
          <!--                                :disabled="form.searchMethod !== 'semantic_search'"-->
          <!--                            />-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                        <div class="config-item">-->
          <!--                          <div class="config-label">-->
          <!--                            <span>Score 阈值</span>-->
          <!--                            <el-tooltip content="设置相似度分数的最小阈值">-->
          <!--                              <img class="icon-question" src="@/assets/kmc/QuestionFilled.png" alt="" />-->
          <!--                            </el-tooltip>-->
          <!--                            <el-switch v-model="vectorData.scoreThresholdEnabled" class="switch-right"/>-->
          <!--                          </div>-->
          <!--                          <div class="config-content">-->
          <!--                            <el-input-number-->
          <!--                                v-model="vectorData.scoreThreshold"-->
          <!--                                :min="0"-->
          <!--                                :max="1"-->
          <!--                                :step="0.1"-->
          <!--                                controls-position="right"-->
          <!--                                class="input-number"-->
          <!--                                :disabled="!vectorData.scoreThresholdEnabled"-->
          <!--                            />-->
          <!--                            <el-slider-->
          <!--                                v-model="vectorData.scoreThreshold"-->
          <!--                                :min="0"-->
          <!--                                :max="1"-->
          <!--                                :step="0.1"-->
          <!--                                class="slider"-->
          <!--                                :disabled="!vectorData.scoreThresholdEnabled"-->
          <!--                            />-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                      </div>-->
          <!--                    </div>-->
          <!--                  </div>-->

          <!--                  &lt;!&ndash; 全文检索 &ndash;&gt;-->
          <!--                  <div-->
          <!--                      class="search-item"-->
          <!--                      :class="{ act: form.searchMethod === 'full_text_search' }"-->
          <!--                      @click="form.searchMethod = 'full_text_search'"-->
          <!--                      v-if="form.indexingTechnique !== 'economy'"-->
          <!--                  >-->
          <!--                    <div class="search-title">-->
          <!--                      <el-icon class="icon">-->
          <!--                        <img src="@/assets/kmc/Document.png" alt="" v-if="form.searchMethod !== 'full_text_search'"/>-->
          <!--                        <img src="@/assets/kmc/Document2.png" alt="" v-if="form.searchMethod == 'full_text_search'"/>-->
          <!--                      </el-icon>-->
          <!--                      <span>全文检索</span>-->
          <!--                    </div>-->
          <!--                    <div class="search-desc">-->
          <!--                      通过生成查询嵌入并查询与其向量表示最相似的文本分段-->
          <!--                    </div>-->
          <!--                    <div class="search-options">-->
          <!--                      <el-form-item class="form-item-inline" prop="rerankingEnable">-->
          <!--                        <div class="option-label">Rerank 模型</div>-->
          <!--                        <el-switch v-model="fullTextData.rerankingEnable"/>-->
          <!--                      </el-form-item>-->
          <!--                      <el-form-item class="form-item-full" prop="rerankingModelName">-->
          <!--                        <el-select-->
          <!--                            v-model="fullTextData.rerankingModelName"-->
          <!--                            placeholder="请选择模型"-->
          <!--                            :disabled="!fullTextData.rerankingEnable"-->
          <!--                        >-->
          <!--                          <el-option-group-->
          <!--                              v-for="group in rerankingModel"-->
          <!--                              :key="group.label.zh_Hans"-->
          <!--                              :label="group.label.zh_Hans"-->
          <!--                          >-->
          <!--                            <el-option-->
          <!--                                v-for="item in group.models"-->
          <!--                                :key="item.model"-->
          <!--                                :label="item.model"-->
          <!--                                :value="item.model"-->
          <!--                            />-->
          <!--                          </el-option-group>-->
          <!--                        </el-select>-->
          <!--                      </el-form-item>-->
          <!--                      <div class="search-config">-->
          <!--                        <div class="config-item">-->
          <!--                          <div class="config-label">-->
          <!--                            <span>Top K</span>-->
          <!--                            <el-tooltip content="选择返回的相似文本数量">-->
          <!--                              <img class="icon-question" src="@/assets/kmc/QuestionFilled.png" alt="" />-->
          <!--                            </el-tooltip>-->
          <!--                          </div>-->
          <!--                          <div class="config-content">-->
          <!--                            <el-input-number-->
          <!--                                v-model="fullTextData.topK"-->
          <!--                                :min="1"-->
          <!--                                :max="10"-->
          <!--                                controls-position="right"-->
          <!--                                class="input-number"-->
          <!--                            />-->
          <!--                            <el-slider-->
          <!--                                v-model="fullTextData.topK"-->
          <!--                                :min="1"-->
          <!--                                :max="10"-->
          <!--                                class="slider"-->
          <!--                                :disabled="form.searchMethod !== 'full_text_search'"-->
          <!--                            />-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                        <div class="config-item">-->
          <!--                          <div class="config-label">-->
          <!--                            <span>Score 阈值</span>-->
          <!--                            <el-tooltip content="设置相似度分数的最小阈值">-->
          <!--                              <img class="icon-question" src="@/assets/kmc/QuestionFilled.png" alt="" />-->
          <!--                            </el-tooltip>-->
          <!--                            <el-switch v-model="fullTextData.scoreThresholdEnabled" class="switch-right"/>-->
          <!--                          </div>-->
          <!--                          <div class="config-content">-->
          <!--                            <el-input-number-->
          <!--                                v-model="fullTextData.scoreThreshold"-->
          <!--                                :min="0"-->
          <!--                                :max="1"-->
          <!--                                :step="0.1"-->
          <!--                                controls-position="right"-->
          <!--                                class="input-number"-->
          <!--                                :disabled="!fullTextData.scoreThresholdEnabled"-->
          <!--                            />-->
          <!--                            <el-slider-->
          <!--                                v-model="fullTextData.scoreThreshold"-->
          <!--                                :min="0"-->
          <!--                                :max="1"-->
          <!--                                :step="0.1"-->
          <!--                                class="slider"-->
          <!--                                :disabled="!fullTextData.scoreThresholdEnabled"-->
          <!--                            />-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                      </div>-->
          <!--                    </div>-->
          <!--                  </div>-->

          <!--                  &lt;!&ndash; 混合检索 &ndash;&gt;-->
          <!--                  <div-->
          <!--                      class="search-item"-->
          <!--                      :class="{ act: form.searchMethod === 'hybrid_search' }"-->
          <!--                      @click="form.searchMethod = 'hybrid_search'"-->
          <!--                      v-if="form.indexingTechnique !== 'economy'"-->
          <!--                  >-->
          <!--                    <div class="search-title">-->
          <!--                      <div class="search-recommend">-->
          <!--                        <el-icon class="icon">-->
          <!--                          <img src="@/assets/kmc/Files.png" alt="" v-if="form.searchMethod !== 'hybrid_search'"/>-->
          <!--                          <img src="@/assets/kmc/Files2.png" alt="" v-if="form.searchMethod == 'hybrid_search'"/>-->
          <!--                        </el-icon>-->
          <!--                        <span>混合检索</span>-->
          <!--                        <el-tag size="small" type="primary" class="recommend-tag">推荐</el-tag>-->
          <!--                      </div>-->
          <!--                    </div>-->
          <!--                    <div class="search-desc">-->
          <!--                      通过生成查询嵌入并查询与其向量表示最相似的文本分段-->
          <!--                    </div>-->
          <!--                    <div class="search-options">-->
          <!--                      <el-form-item class="form-item-full" prop="rerankingMode">-->
          <!--                        <div class="mode-selector">-->
          <!--                          <div-->
          <!--                              class="mode-item"-->
          <!--                              :class="{ act: mixData.rerankingMode === 'weighted_score' }"-->
          <!--                              @click="mixData.rerankingMode = 'weighted_score'"-->
          <!--                          >-->

          <!--                            <div class="img-title-radio">-->
          <!--                            <img src="@/assets/kmc/weight.png" alt="" class="mode-icon" />-->
          <!--                              <div class="text-radio">-->
          <!--                            <div class="mode-text">-->
          <!--                              <div class="mode-title">权重设置</div>-->
          <!--                            </div>-->
          <!--                            <el-radio-->
          <!--                                v-model="mixData.rerankingMode"-->
          <!--                                label="weighted_score"-->
          <!--                            ></el-radio>-->
          <!--                              </div>-->
          <!--                            </div>-->
          <!--                            <div class="mode-desc">-->
          <!--                              通过调整分配的权重，重新排序策略确定是优先进行语义匹配还是关键字匹配。-->
          <!--                            </div>-->
          <!--                          </div>-->
          <!--                          <div-->
          <!--                              class="mode-item"-->
          <!--                              :class="{ act: mixData.rerankingMode === 'reranking_model' }"-->
          <!--                              @click="mixData.rerankingMode = 'reranking_model'"-->
          <!--                          >-->
          <!--                            <div class="img-title-radio">-->
          <!--                            <img src="@/assets/kmc/share.png" alt="" class="mode-icon" />-->
          <!--                            <div class="mode-text">-->
          <!--                              <div class="mode-title">Rerank模型</div>-->
          <!--                            </div>-->
          <!--                            <el-radio-->
          <!--                                v-model="mixData.rerankingMode"-->
          <!--                                label="reranking_model"-->
          <!--                            ></el-radio>-->
          <!--                            </div>-->
          <!--                            <div class="mode-desc">-->
          <!--                              重排序模型将根据候选文档列表与用户问题语义匹配度进行重新排序。-->
          <!--                            </div>-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                      </el-form-item>-->
          <!--                      <el-form-item-->
          <!--                          class="form-item-full"-->
          <!--                          prop="rerankingModelName"-->
          <!--                          v-if="mixData.rerankingMode === 'reranking_model'"-->
          <!--                      >-->
          <!--                        <div class="option-label">Rerank 模型</div>-->
          <!--                        <el-select-->
          <!--                            v-model="mixData.rerankingModelName"-->
          <!--                            placeholder="请选择模型"-->
          <!--                            :disabled="!mixData.rerankingEnable"-->
          <!--                        >-->
          <!--                          <el-option-group-->
          <!--                              v-for="group in rerankingModel"-->
          <!--                              :key="group.label.zh_Hans"-->
          <!--                              :label="group.label.zh_Hans"-->
          <!--                          >-->
          <!--                            <el-option-->
          <!--                                v-for="item in group.models"-->
          <!--                                :key="item.model"-->
          <!--                                :label="item.model"-->
          <!--                                :value="item.model"-->
          <!--                            />-->
          <!--                          </el-option-group>-->
          <!--                        </el-select>-->
          <!--                      </el-form-item>-->
          <!--                      <el-form-item-->
          <!--                          class="form-item-full"-->
          <!--                          prop="keywordWeight"-->
          <!--                          v-if="mixData.rerankingMode !== 'reranking_model'"-->
          <!--                      >-->
          <!--                        <div class="option-label">权重分配</div>-->
          <!--                        <div class="weight-config">-->
          <!--                          <el-slider-->
          <!--                              v-model="mixData.keywordWeight"-->
          <!--                              :min="0"-->
          <!--                              :max="1.0"-->
          <!--                              :step="0.1"-->
          <!--                              class="weight-slider"-->
          <!--                          ></el-slider>-->
          <!--                          <div class="weight-desc">-->
          <!--                            <div>语义 {{ mixData.keywordWeight }}</div>-->
          <!--                            <div class="keyword-weight">关键词 {{ mixData.vectorWeight }}</div>-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                      </el-form-item>-->
          <!--                      <div class="search-config">-->
          <!--                        <div class="config-item">-->
          <!--                          <div class="config-label">-->
          <!--                            <span>Top K</span>-->
          <!--                            <el-tooltip content="选择返回的相似文本数量">-->
          <!--                              <img class="icon-question" src="@/assets/kmc/QuestionFilled.png" alt="" />-->
          <!--                            </el-tooltip>-->
          <!--                          </div>-->
          <!--                          <div class="config-content">-->
          <!--                            <el-input-number-->
          <!--                                v-model="mixData.topK"-->
          <!--                                :min="1"-->
          <!--                                :max="10"-->
          <!--                                controls-position="right"-->
          <!--                                class="input-number"-->
          <!--                            />-->
          <!--                            <el-slider-->
          <!--                                v-model="mixData.topK"-->
          <!--                                :min="1"-->
          <!--                                :max="10"-->
          <!--                                class="slider"-->
          <!--                                :disabled="form.searchMethod !== 'hybrid_search'"-->
          <!--                            />-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                        <div class="config-item">-->
          <!--                          <div class="config-label">-->
          <!--                            <span>Score 阈值</span>-->
          <!--                            <el-tooltip content="设置相似度分数的最小阈值">-->
          <!--                              <img class="icon-question" src="@/assets/kmc/QuestionFilled.png" alt="" />-->
          <!--                            </el-tooltip>-->
          <!--                            <el-switch v-model="mixData.scoreThresholdEnabled" class="switch-right"/>-->
          <!--                          </div>-->
          <!--                          <div class="config-content">-->
          <!--                            <el-input-number-->
          <!--                                v-model="mixData.scoreThreshold"-->
          <!--                                :min="0"-->
          <!--                                :max="1"-->
          <!--                                :step="0.1"-->
          <!--                                controls-position="right"-->
          <!--                                class="input-number"-->
          <!--                                :disabled="!mixData.scoreThresholdEnabled"-->
          <!--                            />-->
          <!--                            <el-slider-->
          <!--                                v-model="mixData.scoreThreshold"-->
          <!--                                :min="0"-->
          <!--                                :max="1"-->
          <!--                                :step="0.1"-->
          <!--                                class="slider"-->
          <!--                                :disabled="!mixData.scoreThresholdEnabled"-->
          <!--                            />-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                      </div>-->
          <!--                    </div>-->
          <!--                  </div>-->

          <!--                  &lt;!&ndash; 经济模式下的倒排索引 &ndash;&gt;-->
          <!--                  <div-->
          <!--                      class="search-item"-->
          <!--                      :class="{ act: form.searchMethod === 'semantic_search' }"-->
          <!--                      @click="form.searchMethod = 'semantic_search'"-->
          <!--                      v-if="form.indexingTechnique == 'economy'"-->
          <!--                  >-->
          <!--                    <div class="search-title">-->
          <!--                      <el-icon class="icon">-->
          <!--                        <img src="@/assets/kmc/Search.png" alt="" v-if="form.searchMethod !== 'semantic_search'"/>-->
          <!--                        <img src="@/assets/kmc/Search2.png" alt="" v-if="form.searchMethod == 'semantic_search'"/>-->
          <!--                      </el-icon>-->
          <!--                      <span>倒排索引</span>-->
          <!--                    </div>-->
          <!--                    <div class="search-options">-->
          <!--                      <div class="search-config">-->
          <!--                        <div class="config-item">-->
          <!--                          <div class="config-label">-->
          <!--                            <span>Top K</span>-->
          <!--                            <el-tooltip content="选择返回的相似文本数量">-->
          <!--                              <img class="icon-question" src="@/assets/kmc/QuestionFilled.png" alt="" />-->
          <!--                            </el-tooltip>-->
          <!--                          </div>-->
          <!--                          <div class="config-content">-->
          <!--                            <el-input-number-->
          <!--                                v-model="form.topK"-->
          <!--                                :min="1"-->
          <!--                                :max="10"-->
          <!--                                controls-position="right"-->
          <!--                                class="input-number"-->
          <!--                            />-->
          <!--                            <el-slider-->
          <!--                                v-model="form.topK"-->
          <!--                                :min="1"-->
          <!--                                :max="10"-->
          <!--                                class="slider"-->
          <!--                            />-->
          <!--                          </div>-->
          <!--                        </div>-->
          <!--                      </div>-->
          <!--                    </div>-->
          <!--                  </div>-->
          <!--                </div>-->
          <!--              </el-form-item>-->
          <!--            </el-col>-->
          <!--          </el-row>-->
        </el-form>
      </div>

      <!-- 权限设置区域 -->
      <!--      <div class="clearfix header-text">-->
      <!--        <div class="header-left">-->
      <!--          <div class="blue-bar"></div>-->
      <!--          权限设置-->
      <!--        </div>-->
      <!--      </div>-->
      <!--      <div class="table-role">-->
      <!--        <el-table-->
      <!--            stripe-->
      <!--            ref="roleTableRef"-->
      <!--            v-loading="roleLoading"-->
      <!--            :data="roleList"-->
      <!--            @selection-change="handleSelectionChange"-->
      <!--        >-->
      <!--          <el-table-column type="selection" width="55" align="center" />-->
      <!--          <el-table-column label="角色编号" prop="roleId" align="center" />-->
      <!--          <el-table-column-->
      <!--              label="角色名称"-->
      <!--              prop="roleName"-->
      <!--              align="center"-->
      <!--              :show-overflow-tooltip="true"-->
      <!--          />-->
      <!--          <el-table-column-->
      <!--              label="权限字符"-->
      <!--              prop="roleKey"-->
      <!--              align="center"-->
      <!--              :show-overflow-tooltip="true"-->
      <!--          />-->
      <!--          <el-table-column label="显示顺序" prop="roleSort" align="center" />-->
      <!--          <el-table-column label="创建时间" align="center" prop="createTime" width="160">-->
      <!--          </el-table-column>-->
      <!--        </el-table>-->
      <!--      </div>-->

      <!-- 底部操作按钮 -->
      <div class="dialog-footer">
        <el-button type="default" size="small" plain @click="back">
          取消
        </el-button>
        <el-button type="primary" @click="submitForm"> 保存 </el-button>
      </div>
    </div>
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
import { Trophy } from "@element-plus/icons-vue";

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
  },
});

const vectorData = ref({
  rerankingEnable: null,
  rerankingModelName: null,
  topK: 1,
  scoreThresholdEnabled: false,
  scoreThreshold: 0,
});
const fullTextData = ref({
  rerankingEnable: null,
  rerankingModelName: null,
  topK: 1,
  scoreThresholdEnabled: false,
  scoreThreshold: 0,
});
const mixData = ref({
  rerankingEnable: null,
  rerankingModelName: null,
  topK: 1,
  scoreThresholdEnabled: false,
  scoreThreshold: 0,
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
const roleList = ref([]);
const roleIds = ref();
const roleTableRef = ref(null);
const router = useRouter();
const inputValue = ref("");
const inputVisible = ref(false);
const InputRef = ref(null);
let kbid;
const buttonShow = ref(false); // 控制删除按钮显示
const isAdd = ref(!route.params.kbId); // 无kbId=新增，有=修改
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
    console.log("found", found);
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
    scoreThresholdEnabled: false,
    scoreThreshold: null,
    validFlag: true,
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
  title.value = _id ? "基础设置" : "新增知识库";
  isAdd.value = !_id; // 核心：无ID=新增(true)，有ID=修改(false)
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
        Object.assign(mixData.value, {
          rerankingEnable: form.value.rerankingEnable,
          rerankingModelName: form.value.rerankingModelName,
          topK: form.value.topK,
          scoreThresholdEnabled: form.value.scoreThresholdEnabled,
          scoreThreshold: form.value.scoreThreshold,
          keywordWeight: form.value.keywordWeight,
          vectorWeight: form.value.vectorWeight,
          rerankingMode: form.value.rerankingMode,
        });
      }
      if (form.value.indexingTechnique === "high_quality") {
        isDisabled.value = true;
      }
    });
  }
}

// 提交表单
function submitForm() {
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
    form.value.scoreThresholdEnabled = fullTextData.value.scoreThresholdEnabled;
    form.value.scoreThreshold = fullTextData.value.scoreThreshold;
  } else if (form.value.searchMethod === "hybrid_search") {
    form.value.rerankingEnable = mixData.value.rerankingEnable;
    form.value.rerankingModelName = mixData.value.rerankingModelName;
    form.value.topK = mixData.value.topK;
    form.value.scoreThresholdEnabled = mixData.value.scoreThresholdEnabled;
    form.value.scoreThreshold = mixData.value.scoreThreshold;
    form.value.keywordWeight = mixData.value.keywordWeight;
    form.value.vectorWeight = mixData.value.vectorWeight;
    form.value.rerankingMode = mixData.value.rerankingMode;
  }
  const matchedItem = embeddingModel.value.find((item) =>
    item.models.some((modelObj) => modelObj.model === form.value.embeddingModel)
  );
  form.value.rerankingProviderName = matchedItem ? matchedItem.provider : null;
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
  getRerank().then((response) => {
    rerankingModel.value = response.data;
    if (rerankingModel.value.length) {
      const defaultModel = rerankingModel.value[0].models[0].model;
      vectorData.value.rerankingModelName = defaultModel;
      fullTextData.value.rerankingModelName = defaultModel;
      mixData.value.rerankingModelName = defaultModel;
      vectorData.value.rerankingEnable = false;
      fullTextData.value.rerankingEnable = false;
      mixData.value.rerankingEnable = true;
    }
  });
  getTextEmbedding().then((response) => {
    embeddingModel.value = response.data;
  });
}

// 初始化
init();
</script>

<style scoped lang="scss">
// 基础样式对齐目标页面
:deep(.el-input-number .el-input__inner) {
  text-align: center;
}
:deep(.el-slider) {
  margin-top: 8px;
}
:deep(.space-between .el-form-item__content) {
  justify-content: space-between;
}
::v-deep .el-tag {
  margin-right: 7px;
}
::v-deep .el-divider__text.is-left {
  color: #4b7eed;
}

// 全局容器样式
.app-container {
  .pagecont-top {
    padding: 15px;
    height: 100%;
    // 顶部标题栏样式
    .pagecont-top-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 11px;

      .header-text {
        display: flex;
        align-items: center;
        margin-bottom: 3px;
        margin: 10px 0;

        .header-left {
          display: flex;
          align-items: center;
          .blue-bar {
            background-color: #2666fb;
            width: 6px;
            height: 16px;
            margin-right: 10px;
            border-radius: 3px;
          }
        }
      }
      .currImg {
        display: inline-block;
      }
      .act {
        display: none;
      }

      .el-button {
        img {
          margin-right: 6px;
          width: 12px;
          height: 12px;
        }
        height: 28px;
        width: 80px;
        font-size: 12px !important;

        &:hover {
          .act {
            display: inline-block;
          }
          .currImg {
            display: none;
          }
        }
      }
    }
  }
}

// 表格样式（对齐目标页面）
.table-role {
  margin: 15px 0;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  overflow: hidden;

  :deep(.el-table) {
    border: none;
    .el-table__header-wrapper {
      background-color: #f9fafb;
      th {
        background: transparent;
        border-bottom: 1px solid #e5e7eb;
        font-weight: 500;
        color: #666;
      }
    }
    .el-table__body-wrapper {
      tr {
        &:hover > td {
          background-color: #f9fafb;
        }
      }
      td {
        border-bottom: 1px solid #f1f5f9;
      }
    }
  }
}

// 底部按钮样式（与目标页面一致）
.dialog-footer {
  display: flex;
  justify-content: flex-end;

  .el-button {
    img {
      margin-right: 6px;
      width: 11px;
      height: 11px;
    }
    height: 28px;
    width: 80px;
    font-size: 12px !important;
  }
}

.flex {
  display: flex;
}

// 基础下划线样式（通用）
.underline-base {
  border-bottom: 2px solid #f4f4f4;
  margin-bottom: 13px; // 通用下间距
}

// 新增场景样式
.underline-add {
  border-bottom: 2px solid #f4f4f4;
  margin-top: 210px; // 新增的上间距
}

// 修改场景样式
.underline-edit {
  border-bottom: 2px solid #f4f4f4;
  margin-top: 166px; // 修改的上间距
}

// 索引方式样式（对齐目标页面风格）
.indexing-container {
  display: flex;
  gap: 15px;
  width: 100%;
}
.indexing-item {
  flex: 1;
  padding: 15px;
  border-radius: 4px;
  border: 1px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #f9fafb;

  &.act {
    background-color: #f0f7ff;
    border-color: #2666fb;
    .indexing-title {
      color: #2666fb;
      font-weight: 500;
    }
  }
  &.disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}
.indexing-title {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #333;
  font-size: 14px;

  .icon {
    margin-right: 8px;
    font-size: 18px;
    color: #666;
  }
  img {
    width: 18px;
    height: 18px;
    margin-right: 8px;
  }
}
.indexing-desc {
  margin: 0;
  font-size: 12px;
  color: #666;
  line-height: 1.5;
}
.recommend-tag {
  margin-left: auto;
  height: 20px;
  line-height: 20px;
  padding: 0 6px;
  font-size: 12px;
}

// 检索设置样式（统一风格）
.search-container {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  width: 100%;
  padding: 10px 0;
}
.search-item {
  flex: 1;
  min-width: 280px;
  padding: 15px;
  border-radius: 4px;
  border: 1px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #f9fafb;

  &.act {
    background-color: #f0f7ff;
    border-color: #2666fb;
    .search-title {
      color: #2666fb;
      font-weight: 500;
    }
  }
}
.search-title {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  color: #333;
  font-size: 14px;

  .icon {
    margin-right: 8px;
    font-size: 18px;
  }
  img {
    width: 18px;
    height: 18px;
    margin-right: 8px;
  }
}
.search-recommend {
  display: flex;
  align-items: center;
  width: 100%;
}
.search-desc {
  margin: 0 0 15px 0;
  font-size: 12px;
  color: #666;
  line-height: 1.5;
}
.search-options {
  margin-top: 10px;
}

// 表单项样式统一
.form-item-inline {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  :deep(.el-form-item__label) {
    display: none;
  }
  :deep(.el-form-item__content) {
    width: 100%;
  }
}
.form-item-full {
  margin-bottom: 12px;
  :deep(.el-form-item__label) {
    display: none;
  }
  :deep(.el-form-item__content) {
    width: 100%;
  }
}
.option-label {
  font-size: 12px;
  color: #666;
  margin-right: 10px;
  white-space: nowrap;
}

// 检索配置项样式
.search-config {
  margin-top: 15px;
  border-top: 1px dashed #e5e7eb;
  padding-top: 15px;
}
.config-item {
  margin-bottom: 15px;
}
.config-label {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 12px;
  color: #666;

  .icon-question {
    width: 14px;
    height: 14px;
    margin-left: 5px;
  }
}
.config-content {
  display: flex;
  align-items: center;
  gap: 15px;
}
.input-number {
  width: 120px !important;
}
.slider {
  flex: 1;
  :deep(.el-slider__runway) {
    height: 6px;
  }
  :deep(.el-slider__button) {
    width: 14px;
    height: 14px;
  }
}
.switch-right {
  margin-left: auto;
}

// 混合检索模式选择样式
.mode-selector {
  display: flex;
  gap: 10px;
  width: 100%;
}
.mode-item {
  flex: 1;
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 10px;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  flex-direction: column;
  .img-title-radio {
    display: flex;
    margin-bottom: -10px;
    width: 100%;
    .text-radio {
      display: flex;
      justify-content: space-between;
      flex: 1;
    }
  }

  &.act {
    border-color: #2666fb;
    background-color: #f0f7ff;
  }
}
.mode-icon {
  width: 24px;
  height: 24px;
  margin-top: 7px;
  margin-right: 15px;
}
.mode-text {
  flex: 1;
}
.mode-title {
  font-size: 13px;
  color: #2666fb;
  margin-bottom: 5px;
  //margin-right: 60px;
}
.mode-desc {
  font-size: 12px;
  color: #666;
  line-height: 1.7;
  margin-left: 40px;
  margin-top: -5px;
  padding-right: 20px;
}

// 权重配置样式
.weight-config {
  width: 100%;
  padding: 10px;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
}
.weight-slider {
  width: 100%;
  margin-bottom: 8px;
}
.weight-desc {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #666;
}
.keyword-weight {
  color: #2666fb;
}

// 单选按钮样式统一（对齐目标页面）
:deep(.el-radio-group) {
  display: flex;
  gap: 15px;
  margin-top: 5px;

  .el-radio {
    display: flex;
    align-items: center;
    gap: 5px;
    font-size: 13px;

    :deep(.el-radio__input.is-checked .el-radio__inner) {
      border-color: #2666fb;
      background-color: #2666fb;
    }
    :deep(.el-radio__input.is-checked + .el-radio__label) {
      color: #2666fb;
    }
  }
}

// 下拉选择框样式统一
:deep(.el-select) {
  width: 100%;
  :deep(.el-input__inner) {
    height: 32px;
    line-height: 32px;
    font-size: 13px;
  }
}

//:deep(.el-radio__label) {
//  display: none !important;
//}

:deep(.el-form-item__label) {
  margin-right: 20px; // 自定义右边距（默认一般是 8px/10px）
}

//// 穿透修改 el-form-item__label-wrap 的 margin
//:deep(.name-form-item .el-form-item__label-wrap) {
//  // 示例1：重置所有 margin（清除默认间距）
//  margin-left: 20px !important;
//  margin-right: 16px !important;
//}
//
:deep(.name-form-item .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}
//
//:deep(.image-form-item .el-form-item__label-wrap) {
//  // 示例1：重置所有 margin（清除默认间距）
//  margin-left: 30px !important;
//  margin-right: 1px !important;
//}
//
:deep(.image-form-item .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}
//
//:deep(.tag-form-item .el-form-item__label-wrap) {
//  // 示例1：重置所有 margin（清除默认间距）
//  margin-left: 30px !important;
//  margin-right: 15px !important;
//}
//
:deep(.tag-form-item .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}
//
//
//:deep(.status-form-item .el-form-item__label-wrap) {
//  // 示例1：重置所有 margin（清除默认间距）
//  margin-left: 20px !important;
//  margin-right: 15px !important;
//
//}
//
:deep(.status-form-item .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}
//
//
//:deep(.desc-form-item .el-form-item__label-wrap) {
//  // 示例1：重置所有 margin（清除默认间距）
//  margin-left: 30px !important;
//  margin-right: 15px !important;
//}
//
:deep(.desc-form-item .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}
//
//:deep(.index-form-item .el-form-item__label-wrap) {
//  // 示例1：重置所有 margin（清除默认间距）
//  margin-left: 20px !important;
//
//  margin-right: -13px !important;
//}
:deep(.index-form-item .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}
//
//:deep(.emdedding-form-item .el-form-item__label-wrap) {
//  // 示例1：重置所有 margin（清除默认间距）
//  margin-left: 20px !important;
//}
//
:deep(.emdedding-form .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}
</style>
