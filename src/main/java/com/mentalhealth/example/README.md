# 工单实体类示例

这个目录包含了工单实体类的示例实现，展示了如何设计一个完整的工单系统实体。

## 实体类说明

### SampleTicket.java
这是主要的工单实体类，包含以下属性：
- `id`: 主键，自增长
- `ticketNo`: 工单编号，唯一标识
- `title`: 工单标题
- `content`: 工单内容（文本类型）
- `status`: 工单状态（枚举类型）
- `category`: 工单类别（枚举类型）
- `priority`: 工单优先级（枚举类型）
- `studentId`: 学生ID（外键）
- `counselorId`: 咨询师ID（外键，可为空）
- `createTime`: 创建时间
- `updateTime`: 更新时间
- `isDeleted`: 软删除标记

### 枚举类

#### TicketStatus.java
工单状态枚举，包含：
- PENDING: 待受理
- IN_PROGRESS: 咨询中
- COMPLETED: 已完成
- CLOSED: 已关闭

#### TicketCategory.java
工单类别枚举，包含：
- STUDY: 学习压力
- RELATIONSHIP: 人际关系
- EMOTION: 情绪问题
- CAREER: 职业规划
- FAMILY: 家庭关系
- LOVE: 恋爱问题
- OTHER: 其他问题

#### TicketPriority.java
工单优先级枚举，包含：
- LOW: 低
- MEDIUM: 中
- HIGH: 高

## 使用说明

这些类是作为示例提供的，展示了良好的实体设计实践：
1. 使用枚举类型管理状态、类别和优先级
2. 使用@JsonFormat注解格式化日期时间
3. 使用JPA注解进行ORM映射
4. 包含适当的约束注解（如@NotNull, @Column等）

在实际项目中，您可能需要根据具体需求调整这些类。