# 泵站故障检测假数据示例

## 示例 1：正常运行的泵站

```json
{
  "pump_station_name": "城东排水泵站",
  "pump_no": "P-001",
  "pump_model": "300QW800-15-55",
  "inspect_time": "2026-06-18 10:00:00",
  "inspector": "张三",
  "running_hours": 3200,
  "rated_current": 102,
  "actual_current": 101.5,
  "rated_voltage": 380,
  "actual_voltage": 378,
  "rated_power": 55,
  "actual_power": 54.2,
  "rated_flow": 800,
  "actual_flow": 792,
  "rated_head": 15,
  "actual_head": 14.8,
  "rated_speed": 1470,
  "actual_speed": 1468,
  "actual_vibration": 2.8,
  "actual_motor_temp": 58,
  "actual_bearing_temp": 48,
  "actual_inlet_pressure": 0.05,
  "rated_outlet_pressure": 0.18,
  "actual_outlet_pressure": 0.177,
  "abnormal_desc": "无异常声响，运行平稳"
}
```

**预期判断结果**：正常

---

## 示例 2：轴承磨损导致振动和温度异常

```json
{
  "pump_station_name": "城西提升泵站",
  "pump_no": "P-002",
  "pump_model": "250QW600-20-45",
  "inspect_time": "2026-06-18 11:30:00",
  "inspector": "李四",
  "running_hours": 12500,
  "rated_current": 84,
  "actual_current": 86.3,
  "rated_voltage": 380,
  "actual_voltage": 379,
  "rated_power": 45,
  "actual_power": 47.1,
  "rated_flow": 600,
  "actual_flow": 585,
  "rated_head": 20,
  "actual_head": 19.5,
  "rated_speed": 1470,
  "actual_speed": 1469,
  "actual_vibration": 9.2,
  "actual_motor_temp": 72,
  "actual_bearing_temp": 88,
  "actual_inlet_pressure": 0.04,
  "rated_outlet_pressure": 0.22,
  "actual_outlet_pressure": 0.215,
  "abnormal_desc": "泵体有异响，轴承位置温度偏高"
}
```

**预期判断结果**：异常
**命中故障类型**：振动异常、轴承过热
**可能原因**：轴承磨损、润滑不良

---

## 示例 3：进口堵塞导致流量不足

```json
{
  "pump_station_name": "城南雨水泵站",
  "pump_no": "P-003",
  "pump_model": "350QW1100-10-75",
  "inspect_time": "2026-06-18 09:15:00",
  "inspector": "王五",
  "running_hours": 5600,
  "rated_current": 138,
  "actual_current": 110.2,
  "rated_voltage": 380,
  "actual_voltage": 381,
  "rated_power": 75,
  "actual_power": 58.5,
  "rated_flow": 1100,
  "actual_flow": 420,
  "rated_head": 10,
  "actual_head": 8.2,
  "rated_speed": 980,
  "actual_speed": 979,
  "actual_vibration": 3.5,
  "actual_motor_temp": 62,
  "actual_bearing_temp": 55,
  "actual_inlet_pressure": 0.005,
  "rated_outlet_pressure": 0.12,
  "actual_outlet_pressure": 0.08,
  "abnormal_desc": "出口压力低，流量明显下降"
}
```

**预期判断结果**：严重
**命中故障类型**：流量不足 / 无流量、汽蚀
**可能原因**：进口管路堵塞、底阀滤网堵塞、进口液位过低

---

## 示例 4：电机过载

```json
{
  "pump_station_name": "城北循环泵站",
  "pump_no": "P-004",
  "pump_model": "200QW400-30-55",
  "inspect_time": "2026-06-18 14:20:00",
  "inspector": "赵六",
  "running_hours": 8900,
  "rated_current": 102,
  "actual_current": 128.5,
  "rated_voltage": 380,
  "actual_voltage": 377,
  "rated_power": 55,
  "actual_power": 71.3,
  "rated_flow": 400,
  "actual_flow": 510,
  "rated_head": 30,
  "actual_head": 26.5,
  "rated_speed": 1470,
  "actual_speed": 1472,
  "actual_vibration": 5.1,
  "actual_motor_temp": 92,
  "actual_bearing_temp": 70,
  "actual_inlet_pressure": 0.06,
  "rated_outlet_pressure": 0.35,
  "actual_outlet_pressure": 0.31,
  "abnormal_desc": "电机声音沉闷，电流持续偏高"
}
```

**预期判断结果**：严重
**命中故障类型**：电机过载、振动异常
**可能原因**：流量过大、叶轮卡滞、机械密封摩擦增大

---

## 示例 5：电机空转

```json
{
  "pump_station_name": "工业园区泵站",
  "pump_no": "P-005",
  "pump_model": "150QW200-22-22",
  "inspect_time": "2026-06-18 16:00:00",
  "inspector": "孙七",
  "running_hours": 2100,
  "rated_current": 42,
  "actual_current": 18.5,
  "rated_voltage": 380,
  "actual_voltage": 380,
  "rated_power": 22,
  "actual_power": 9.8,
  "rated_flow": 200,
  "actual_flow": 0,
  "rated_head": 22,
  "actual_head": 0,
  "rated_speed": 1470,
  "actual_speed": 1470,
  "actual_vibration": 1.5,
  "actual_motor_temp": 45,
  "actual_bearing_temp": 42,
  "actual_inlet_pressure": 0.03,
  "rated_outlet_pressure": 0.25,
  "actual_outlet_pressure": 0.02,
  "abnormal_desc": "电机在转，但不出水"
}
```

**预期判断结果**：严重
**命中故障类型**：电机欠载 / 空转、流量不足 / 无流量
**可能原因**：出口阀门关闭、联轴器断开、叶轮脱落