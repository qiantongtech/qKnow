# 泵站故障检测报告模板

## 1. 基本信息

| 项目 | 内容 |
|------|------|
| 泵站名称 | {pump_station_name} |
| 水泵编号 | {pump_no} |
| 水泵型号 | {pump_model} |
| 检测时间 | {inspect_time} |
| 检测人员 | {inspector} |
| 运行时长 | {running_hours} h |

## 2. 检测数据汇总

| 参数 | 单位 | 额定值 | 实测值 | 状态 |
|------|------|--------|--------|------|
| 电机电流 | A | {rated_current} | {actual_current} | {status} |
| 电机电压 | V | {rated_voltage} | {actual_voltage} | {status} |
| 电机功率 | kW | {rated_power} | {actual_power} | {status} |
| 流量 | m³/h | {rated_flow} | {actual_flow} | {status} |
| 扬程 | m | {rated_head} | {actual_head} | {status} |
| 转速 | r/min | {rated_speed} | {actual_speed} | {status} |
| 泵体振动速度 | mm/s | ≤4.5 | {actual_vibration} | {status} |
| 电机绕组温度 | ℃ | ≤75 | {actual_motor_temp} | {status} |
| 轴承温度 | ℃ | ≤65 | {actual_bearing_temp} | {status} |
| 进口压力 | MPa | ≥0.02 | {actual_inlet_pressure} | {status} |
| 出口压力 | MPa | {rated_outlet_pressure} | {actual_outlet_pressure} | {status} |

## 3. 故障判断结果

- **综合故障等级**：{fault_level}
- **是否存在故障**：{has_fault}
- **命中故障类型**：{fault_types}

## 4. 故障详情分析

### 4.1 主要故障

- **故障类型**：{primary_fault_type}
- **判断依据**：{judgement_basis}
- **可能原因**：
  - {cause_1}
  - {cause_2}
  - {cause_3}

### 4.2 次要故障 / 异常提示

- {secondary_fault_1}
- {secondary_fault_2}

## 5. 处理建议

| 优先级 | 建议措施 | 建议完成时间 |
|--------|----------|--------------|
| 高 | {high_priority_action} | {high_priority_time} |
| 中 | {medium_priority_action} | {medium_priority_time} |
| 低 | {low_priority_action} | {low_priority_time} |

## 6. 后续跟踪建议

- {follow_up_1}
- {follow_up_2}
- {follow_up_3}

## 7. 备注

- 本报告基于单次检测数据生成，仅供参考。
- 最终处置请以现场工程师判断为准。
- 建议在故障排除后重新检测并归档。