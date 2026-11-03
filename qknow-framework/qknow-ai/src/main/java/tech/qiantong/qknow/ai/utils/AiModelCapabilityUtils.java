package tech.qiantong.qknow.ai.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model capability detection shared by model configuration and runtime calls.
 */
public final class AiModelCapabilityUtils {

    private static final Pattern THINKING_MODEL_PATTERN = Pattern.compile(
            "(^|[^a-z0-9])(o1|o3|o4|qwq|qvq|r1)([^a-z0-9]|$)"
                    + "|deepseek-(reasoner|r1)"
                    + "|(^|[^a-z0-9])qwen3([^a-z0-9]|$)"
                    + "|(^|[^a-z0-9])gpt-5([^a-z0-9]|$)"
                    + "|gpt-oss"
                    + "|glm-(4\\.[5-9]|[5-9])"
                    + "|think|reason"
    );

    private static final Pattern QWEN_VERSION_PATTERN = Pattern.compile(
            "(?:^|[^a-z0-9])qwen(\\d+)(?:\\.(\\d+))?(?:[^a-z0-9]|$)"
    );

    private AiModelCapabilityUtils() {
    }

    public static boolean supportsThinking(String modelName) {
        if (modelName == null || modelName.isBlank()) {
            return false;
        }
        return THINKING_MODEL_PATTERN.matcher(modelName.toLowerCase(Locale.ROOT)).find();
    }

    public static boolean isGlmThinkingModel(String modelName) {
        if (modelName == null || modelName.isBlank()) {
            return false;
        }
        return modelName.toLowerCase(Locale.ROOT).matches(".*glm-(4\\.[5-9]|[5-9]).*");
    }

    /**
     * DashScope native multimodal models use a different endpoint from text-only models.
     */
    public static boolean usesDashScopeMultimodalEndpoint(String modelName) {
        if (modelName == null || modelName.isBlank()) {
            return false;
        }
        String normalized = modelName.toLowerCase(Locale.ROOT);
        if (normalized.contains("omni")
                || normalized.contains("-vl-")
                || normalized.endsWith("-vl")
                || normalized.contains("qwen-audio")
                || normalized.contains("qvq")) {
            return true;
        }

        Matcher matcher = QWEN_VERSION_PATTERN.matcher(normalized);
        if (!matcher.find()) {
            return false;
        }
        int major = Integer.parseInt(matcher.group(1));
        int minor = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2));
        return major > 3 || (major == 3 && minor >= 5);
    }
}
