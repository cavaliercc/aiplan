export interface Store {
  id: string
  name: string
  address: string
  manager: string
  phone: string
  businessHours: string
  status: '营业中' | '休息中' | '装修中'
  employeeCount: number
  area: number
}

export const stores: Store[] = [
  {
    id: 'S001',
    name: '新街口旗舰店',
    address: '南京市玄武区中山路18号德基广场B1层',
    manager: '张明远',
    phone: '025-8888-0001',
    businessHours: '10:00 - 22:00',
    status: '营业中',
    employeeCount: 28,
    area: 520,
  },
  {
    id: 'S002',
    name: '河西万达店',
    address: '南京市建邺区江东中路98号万达广场2层',
    manager: '孙志强',
    phone: '025-8888-0002',
    businessHours: '10:00 - 22:00',
    status: '营业中',
    employeeCount: 22,
    area: 380,
  },
  {
    id: 'S003',
    name: '江宁万达店',
    address: '南京市江宁区双龙大道1680号万达广场1层',
    manager: '黄俊杰',
    phone: '025-8888-0003',
    businessHours: '10:00 - 21:30',
    status: '营业中',
    employeeCount: 18,
    area: 300,
  },
  {
    id: 'S004',
    name: '仙林大学城店',
    address: '南京市栖霞区文苑路9号仙林金鹰购物中心1层',
    manager: '刘建国',
    phone: '025-8888-0004',
    businessHours: '09:30 - 21:30',
    status: '营业中',
    employeeCount: 15,
    area: 260,
  },
  {
    id: 'S005',
    name: '夫子庙店',
    address: '南京市秦淮区贡院西街53号水游城2层',
    manager: '何秀英',
    phone: '025-8888-0005',
    businessHours: '10:00 - 22:00',
    status: '营业中',
    employeeCount: 20,
    area: 340,
  },
  {
    id: 'S006',
    name: '南京南站店',
    address: '南京市雨花台区南站大道1号南京南站B1层',
    manager: '待定',
    phone: '025-8888-0006',
    businessHours: '07:00 - 22:00',
    status: '营业中',
    employeeCount: 12,
    area: 200,
  },
  {
    id: 'S007',
    name: '桥北弘阳店',
    address: '南京市浦口区大桥北路48号弘阳广场1层',
    manager: '待定',
    phone: '025-8888-0007',
    businessHours: '10:00 - 21:00',
    status: '装修中',
    employeeCount: 0,
    area: 280,
  },
  {
    id: 'S008',
    name: '龙江银城店',
    address: '南京市鼓楼区草场门大街101号银城东苑商铺',
    manager: '待定',
    phone: '025-8888-0008',
    businessHours: '09:00 - 21:00',
    status: '休息中',
    employeeCount: 10,
    area: 180,
  },
]
