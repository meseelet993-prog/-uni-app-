# -uni-app-
此为后端部分，xk是前端部分
# 心理健康服务平台

## 项目简介

基于Spring Boot + MySQL的心理健康服务平台后端API，提供用户管理、心理咨询工单管理、实时聊天通信等核心功能。

## 功能特点

### 🧠 核心功能模块

#### 用户管理模块
- **用户注册登录**：JWT Token认证，支持邮箱/手机号注册
- **权限控制**：基于角色的访问控制(RBAC)，区分普通用户、咨询师、管理员
- **个人信息管理**：用户资料维护、头像上传、个人设置
- **安全防护**：密码加密存储、登录失败限制、会话管理

#### 心理咨询工单管理模块
- **工单创建**：用户可提交心理咨询需求，选择咨询类型和紧急程度
- **工单分配**：自动或手动分配给合适的咨询师
- **状态跟踪**：完整的工单生命周期管理（新建→处理中→已完成→已关闭）
- **消息记录**：咨询过程中的所有交流记录保存
- **优先级管理**：支持紧急、高、中、低四级优先级
- **分类管理**：按咨询类型进行分类（焦虑、抑郁、人际关系等）

#### 实时通信模块
- **WebSocket聊天**：支持用户与咨询师实时文字交流
- **消息推送**：新消息实时通知提醒
- **在线状态**：显示用户和咨询师的在线状态
- **历史记录**：完整的消息历史查询和导出

### 🔧 技术特性
- **RESTful API设计**：标准化的HTTP接口
- **数据验证**：完善的输入参数校验
- **异常处理**：统一的错误响应格式
- **日志记录**：详细的系统操作日志
- **性能监控**：健康检查端点和性能指标

## 技术栈

### 后端技术
- **核心框架**：Spring Boot 3.x
- **编程语言**：Java 17+
- **数据库**：MySQL 8.0
- **ORM框架**：MyBatis Plus
- **数据库迁移**：Flyway
- **实时通信**：WebSocket
- **安全框架**：Spring Security + JWT
- **API文档**：Swagger/OpenAPI 3.0

### 开发工具
- **构建工具**：Maven 3.8+
- **代码质量**：Qodana静态分析
- **测试工具**：JUnit 5, Postman
- **开发环境**：IntelliJ IDEA

## 环境要求

### 基础环境
- **JDK**：Java 17 或更高版本
- **数据库**：MySQL 8.0 或更高版本
- **构建工具**：Maven 3.8+
- **操作系统**：Windows/Linux/macOS

### 推荐开发环境
```bash
# 检查Java版本
java -version  # 需要Java 17+

# 检查Maven版本
mvn -version   # 需要Maven 3.8+

# 检查MySQL版本
mysql --version # 需要MySQL 8.0+
```

## 安装步骤

### 1. 克隆项目
```bash
git clone https://github.com/your-username/mental-health-platform.git
cd mental-health-platform
```

### 2. 数据库配置
```sql
-- 创建数据库
CREATE DATABASE mental_health_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户并授权
CREATE USER 'mental_health_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON mental_health_platform.* TO 'mental_health_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. 修改配置文件
编辑 `src/main/resources/application.properties`：
```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/mental_health_platform?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username=mental_health_user
spring.datasource.password=your_password

# JWT密钥（建议修改为复杂字符串）
jwt.secret=your-very-secret-key-change-this-in-production

# 服务器端口
server.port=8082
```

### 4. 安装依赖并构建
```bash
# 清理并编译项目
mvn clean compile

# 运行数据库迁移
mvn flyway:migrate

# 打包项目
mvn package
```

### 5. 初始化数据（可选）
项目包含数据库初始化脚本，在首次运行时会自动创建必要的表结构和基础数据。

## 使用方法

### 启动项目
```bash
# 开发环境直接运行
mvn spring-boot:run

# 或者运行打包后的jar文件
java -jar target/mental-health-platform-1.0.0.jar
```

### API测试

#### 使用Postman测试
1. 导入Postman集合（如果提供）
2. 设置基础URL：`http://localhost:8082`
3. 测试健康检查端点：
   ```
   GET http://localhost:8082/api/health
   ```

#### 基本API调用示例

**1. 用户注册**
```bash
curl -X POST http://localhost:8082/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "role": "USER"
  }'
```

**2. 用户登录**
```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**3. 创建咨询工单**
```bash
curl -X POST http://localhost:8082/api/tickets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "焦虑情绪咨询",
    "description": "最近感到很焦虑，希望能得到专业帮助",
    "categoryId": 1,
    "priority": "HIGH"
  }'
```

### WebSocket实时通信测试
项目提供了一个简单的WebSocket测试页面：
```bash
# 访问测试页面
http://localhost:8082/ws-test.html

# 或使用PowerShell脚本测试
.\scripts\ws-test.ps1
```

## API文档

### Swagger UI
项目集成了Swagger API文档，启动后可通过以下地址访问：
- **API文档**：`http://localhost:8082/swagger-ui.html`
- **API规范**：`http://localhost:8082/v3/api-docs`

### 主要API端点

| 端点 | 方法 | 描述 |
|------|------|------|
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/login` | POST | 用户登录 |
| `/api/users/profile` | GET | 获取用户信息 |
| `/api/tickets` | GET/POST | 工单列表/创建工单 |
| `/api/tickets/{id}` | GET/PUT | 工单详情/更新状态 |
| `/api/tickets/{id}/messages` | GET/POST | 工单消息列表/发送消息 |
| `/api/chat/messages` | GET/POST | 聊天消息 |
| `/api/health` | GET | 健康检查 |

### 请求/响应格式
```json
// 统一响应格式
{
  "success": true,
  "message": "操作成功",
  "data": {},
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 注意事项

### 常见问题及解决方案

#### 1. 数据库连接问题
**问题**：无法连接到MySQL数据库
**解决方案**：
- 检查MySQL服务是否启动
- 确认数据库用户名和密码正确
- 验证数据库是否存在且有相应权限
- 检查防火墙设置

#### 2. 端口占用问题
**问题**：端口8080已被占用
**解决方案**：
```properties
# 在application.properties中修改端口
server.port=8081
```

#### 3. JWT Token过期
**问题**：登录后一段时间API返回401错误
**解决方案**：
- 重新登录获取新的Token
- 检查JWT配置的有效期设置

#### 4. Flyway迁移失败
**问题**：数据库迁移脚本执行失败
**解决方案**：
```bash
# 查看迁移状态
mvn flyway:info

# 修复迁移问题
mvn flyway:repair

# 重新执行迁移
mvn flyway:migrate
```

#### 5. 中文乱码问题
**问题**：数据库中文数据显示乱码
**解决方案**：
- 确保数据库字符集为utf8mb4
- 检查连接字符串中的字符编码设置
- 验证application.properties中的编码配置

### 安全建议

1. **生产环境配置**
   - 修改默认的JWT密钥
   - 启用HTTPS
   - 配置适当的CORS策略
   - 设置合理的会话超时时间

2. **数据保护**
   - 敏感信息加密存储
   - 定期备份数据库
   - 监控异常访问行为

3. **性能优化**
   - 合理设置数据库连接池大小
   - 启用适当的缓存机制
   - 定期清理历史数据

### 开发规范

#### 代码规范
- 遵循Google Java Style Guide
- 使用有意义的变量和方法命名
- 添加必要的代码注释
- 编写单元测试覆盖核心逻辑

#### Git提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建过程或辅助工具的变动
```

### 项目结构说明
```
src/main/java/com/mentalhealth/
├── config/          # 配置类
├── constant/        # 常量定义
├── controller/      # 控制器层
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── mapper/         # 数据访问层
├── middleware/     # 中间件/拦截器
├── service/        # 业务逻辑层
└── websocket/      # WebSocket相关
```

## 贡献指南

欢迎提交Issue和Pull Request来改进项目！

### 开发流程
1. Fork项目
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 提交更改：`git commit -am 'Add some feature'`
4. 推送到分支：`git push origin feature/your-feature`
5. 创建Pull Request

## 许可证

本项目采用MIT许可证 - 查看[LICENSE](LICENSE)文件了解详情

## 联系方式

如有问题或建议，请通过以下方式联系我们：
- 邮箱：1612473807@qq.com
- GitHub Issues：[https://github.com/meseelet993-prog/-uni-app-.git](https://github.com/your-username/mental-health-platform/issues)

---

**注意**：这是一个演示项目，不应用于生产环境的心理健康服务。如需专业心理健康服务，请咨询持证专业人士。
