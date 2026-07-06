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

import { Shape } from '@antv/x6';

/**
 * antv x6的所有配置项整合
 */
export const baseConfig = {
    // 设置节点移动的范围在画布内
    translating: {
        restrict: true
    },
    grid: true,
    background: {
        color: 'transparent'
    },
    mousewheel: {
        enabled: true,
        zoomAtMousePosition: true,
        modifiers: 'ctrl',
        minScale: 0.5,
        maxScale: 3
    },
    scaling: {
        min: 0.5,
        max: 2
    },
    panning: {
        enabled: true,
        modifiers: 'alt'
    },
    connecting: {
        router: {
            name: 'manhattan',
            args: {
                padding: 10, // 控制边缘与节点之间的间距
                avoid: true // 启用避开节点功能
            }
        },
        connector: {
            name: 'rounded',
            args: {
                radius: 8
            }
        },
        connectionPoint: 'anchor',
        allowBlank: false,
        allowMulti: false,
        allowLoop: false,
        snap: {
            radius: 20
        },
        createEdge() {
            return new Shape.Edge({
                attrs: {
                    line: {
                        stroke: '#409eff',
                        strokeWidth: 1,
                        targetMarker: {
                            name: 'block',
                            width: 12,
                            height: 8
                        }
                    }
                },
                zIndex: '-1'
            });
        },
        validateConnection({ targetMagnet, sourceCell, targetCell, graph }) {
            // 确保目标磁铁存在
            if (!targetMagnet) return false;

            const sourceData = sourceCell.getData();
            const targetData = targetCell.getData();

            let warningMessage = null;

            console.log('🚀 ~ validateConnection ~ targetCell.getData().type:', targetData.type);

            // 限制 1：输入节点只能连接，不能被连接
            if (sourceData.type === 'input' && targetData.type !== 'output') {
                return true; // 允许从输入节点连接到其他节点
            }

            if (targetData.type === 'input' && !targetData.hasWarned) {
                warningMessage = '输入节点不能被连接';
                targetData.hasWarned = true;
            }

            // 限制 2：输出节点只能作为目标节点，不能作为源节点连接
            if (sourceData.type === 'output' && !sourceData.hasWarned) {
                warningMessage = '输出节点只能作为目标节点连接';
                sourceData.hasWarned = true;
            }
            // 显示警告消息
            if (warningMessage) {
                ElMessage.warning(warningMessage);
                return false; // 取消连接
            }

            return true;
        }
    },
    highlighting: {
        magnetAdsorbed: {
            name: 'stroke',
            args: {
                attrs: {
                    fill: '#028FA6',
                    stroke: '#028FA6'
                }
            }
        }
    },
    interacting: function (cellView) {
        if (cellView.cell.getData() != undefined && !cellView.cell.getData().disableMove) {
            return { nodeMovable: false };
        }
        return true;
    },
    // 快捷键绑定
    bindShortcuts(graph) {
        graph.bindKey(['meta+x', 'ctrl+x'], () => {
            const cells = graph.getSelectedCells();
            if (cells.length) {
                graph.cut(cells);
            }
            return false;
        });

        graph.bindKey(['meta+v', 'ctrl+v'], () => {
            paste();
        });

        graph.bindKey(['meta+z', 'ctrl+z'], () => {
            undo();
        });

        graph.bindKey(['meta+y', 'ctrl+y'], () => {
            redo();
        });

        graph.bindKey(['meta+a', 'ctrl+a'], () => {
            const nodes = graph.getNodes();
            if (nodes) {
                graph.select(nodes);
            }
        });

        graph.bindKey('backspace', () => {
            del();
        });

        graph.bindKey(['ctrl+1', 'meta+1'], () => {
            const zoom = graph.zoom();
            if (zoom < 1.5) {
                graph.zoom(0.1);
            }
        });

        graph.bindKey(['ctrl+2', 'meta+2'], () => {
            const zoom = graph.zoom();
            if (zoom > 0.5) {
                graph.zoom(-0.1);
            }
        });
    }
};

// 公用的连接桩
export const cuPort = {
    groups: {
        top: {
            position: 'top',
            attrs: {
                circle: {
                    r: 4,
                    magnet: true,
                    stroke: '#5F95FF',
                    strokeWidth: 1,
                    fill: '#fff',
                    style: { visibility: 'hidden' }
                }
            }
        },
        right: {
            position: 'right',
            attrs: {
                circle: {
                    r: 4,
                    magnet: true,
                    stroke: '#5F95FF',
                    strokeWidth: 1,
                    fill: '#fff',
                    style: { visibility: 'hidden' }
                }
            }
        },
        bottom: {
            position: 'bottom',
            attrs: {
                circle: {
                    r: 4,
                    magnet: true,
                    stroke: '#5F95FF',
                    strokeWidth: 1,
                    fill: '#fff',
                    style: { visibility: 'hidden' }
                }
            }
        },
        left: {
            position: 'left',
            attrs: {
                circle: {
                    r: 4,
                    magnet: true,
                    stroke: '#5F95FF',
                    strokeWidth: 1,
                    fill: '#fff',
                    style: { visibility: 'hidden' }
                }
            }
        }
    },
    items: [{ group: 'top' }, { group: 'right' }, { group: 'bottom' }, { group: 'left' }]
};
