// axios 全局配置：请求后端的基础地址
axios.defaults.baseURL = 'http://localhost:8080';

new Vue({
    el: '#app',
    data: {
        // 部门列表
        deptList: [],
        // 新增弹窗是否可见
        dialogVisible: false,
        // 新增表单数据
        addForm: {
            name: ''
        },
        // 表单校验规则
        addRules: {
            name: [
                { required: true, message: '请输入部门名称', trigger: 'blur' },
                { min: 1, max: 50, message: '部门名称长度在 1 到 50 个字符', trigger: 'blur' }
            ]
        }
    },
    // 页面加载时查询所有部门
    created() {
        this.findAll();
    },
    methods: {
        // 查询所有部门
        async findAll() {
            try {
                const res = await axios.get('/depts');
                if (res.data.code === 1) {
                    this.deptList = res.data.data;
                } else {
                    this.$message.error(res.data.msg || '查询失败');
                }
            } catch (e) {
                this.$message.error('后端服务未启动或请求失败');
            }
        },

        // 新增部门
        addDept() {
            this.$refs.addForm.validate(async (valid) => {
                if (!valid) return;

                try {
                    const res = await axios.post('/depts', this.addForm);
                    if (res.data.code === 1) {
                        this.$message.success('新增成功');
                        this.dialogVisible = false;
                        this.findAll();
                    } else {
                        this.$message.error(res.data.msg || '新增失败');
                    }
                } catch (e) {
                    this.$message.error('新增失败，后端服务未启动或请求失败');
                }
            });
        },

        // 删除部门
        deleteDept(row) {
            this.$confirm(`确定删除部门「${row.name}」吗？`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    const res = await axios.delete(`/depts/${row.id}`);
                    if (res.data.code === 1) {
                        this.$message.success('删除成功');
                        this.findAll();
                    } else {
                        this.$message.error(res.data.msg || '删除失败');
                    }
                } catch (e) {
                    this.$message.error('删除失败，后端服务未启动或请求失败');
                }
            }).catch(() => {
                this.$message.info('已取消删除');
            });
        },

        // 关闭弹窗后重置表单
        resetForm() {
            this.$refs.addForm.resetFields();
        },

        // 返回
        goBack() {
            this.$message.info('返回按钮（可自行扩展）');
        }
    }
});