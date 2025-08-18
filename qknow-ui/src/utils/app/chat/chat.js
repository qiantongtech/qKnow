/**
 * 渲染内容
 * @param content
 * @returns {*|string}
 */
export const renderContent = (content) => {
    const startTag = '<think';
    const endTag = '</think>';
    const startIndex = content.indexOf(startTag);

    if (startIndex === -1) {
        return content
    }

    const afterStart = content.substring(startIndex);
    const endIndex = afterStart.indexOf(endTag);

    let remainingContent = '';

    if (endIndex !== -1) {
        const end = endIndex + endTag.length;
        remainingContent = content.substring(0, startIndex) + afterStart.substring(end);
    } else {
        remainingContent = content.substring(0, startIndex);
    }
    return remainingContent;
}

export const getFileFormat = (filename) => {
    // 获取最后一个点的位置
    const lastDotIndex = filename.lastIndexOf('.');

    // 如果没有点或点是第一个字符，返回空字符串
    if (lastDotIndex === -1 || lastDotIndex === 0) {
        return '';
    }

    // 提取扩展名并转为小写
    return filename.slice(lastDotIndex + 1).toLowerCase();
}
