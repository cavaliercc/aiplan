export const kpiData = [
  {
    title: '本周总排班',
    value: '1,248',
    unit: '小时',
    change: 5.2,
    icon: 'Clock',
    color: '#409eff',
  },
  {
    title: '预估人力成本',
    value: '¥186,400',
    unit: '',
    change: -2.1,
    icon: 'Money',
    color: '#67c23a',
  },
  {
    title: '平均P/H效率',
    value: '¥156',
    unit: '/h',
    change: 3.8,
    icon: 'TrendCharts',
    color: '#e6a23c',
  },
  {
    title: '今日出勤率',
    value: '94.5',
    unit: '%',
    change: 0,
    icon: 'UserFilled',
    color: '#f56c6c',
    isProgress: true,
    progressValue: 94.5,
  },
]

export const weeklyTrend = {
  days: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
  scheduled: [180, 175, 182, 178, 190, 210, 133],
  actual: [172, 170, 180, 175, 185, 205, 128],
}

export const todayAlerts = [
  {
    type: 'warning',
    title: '换班申请',
    desc: '李婷婷申请与周小燕换班（4月25日）',
    time: '10分钟前',
    icon: 'Switch',
  },
  {
    type: 'danger',
    title: '人员配置不足',
    desc: '河西万达店 4月26日晚班缺少1名收银员',
    time: '30分钟前',
    icon: 'Warning',
  },
  {
    type: 'info',
    title: '考勤异常',
    desc: '吴大伟 4月24日迟到15分钟，已自动记录',
    time: '1小时前',
    icon: 'InfoFilled',
  },
  {
    type: 'success',
    title: '排班已发布',
    desc: '新街口旗舰店 第18周排班已发布',
    time: '2小时前',
    icon: 'SuccessFilled',
  },
]
