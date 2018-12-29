var prefix = "/archiveCheck/tProject"
$(function () {
    load();
});


function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/list", // 服务器数据的加载地址
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize: 'outline',
                toolbar: '#exampleToolbar',
                striped: true, // 设置为true会有隔行变色效果
                dataType: "json", // 服务器返回的数据类型
                pagination: true, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect: false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize: 10, // 如果设置了分页，每页数据条数
                pageNumber: 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParams: function (params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit: params.limit,
                        offset: params.offset
                        // name:$('#searchName').val(),
                        // username:$('#searchName').val()
                    };
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns: [
                    {
                        checkbox: true
                    },
                    {
                        field: 'proName',
                        title: '项目名字'
                    },
                    {
                        field: 'path',
                        title: '路径'
                    }
                    //, {
                    //     title: '操作',
                    //     field: 'id',
                    //     align: 'center',
                    //     formatter: function (value, row, index) {
                    //         var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                    //             + row.id
                    //             + '\')"><i class="fa fa-edit"></i></a> ';
                    //         var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                    //             + row.id
                    //             + '\')"><i class="fa fa-remove"></i></a> ';
                    //         var f = '<a class="btn btn-success btn-sm" href="#" title="备用"  mce_href="#" onclick="resetPwd(\''
                    //             + row.id
                    //             + '\')"><i class="fa fa-key"></i></a> ';
                    //         return e + d;
                    //     }
                    // }
                ]
            });
}

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}

/**
 * 抽离出来处理是否开源、是否归档的代码
 * @param obj
 */
function open_or_archive(is_open, is_archive) {

}

function add() {
    // 获取是否开源的所有复选框
    var is_open = $("[name='is_open']");
    // 获取是否归档的所有复选框
    var is_archive = $("[name='is_archive']");
    var content = "";
    // 判断状态
    var state;
    if ($(":checkbox[name=is_open]:checked").size() != 0) {
        for (var i = 0; i < is_archive.length; i++) {
            // 2 标志是勾选的是否归档
            if (is_archive[i].checked && is_archive[i].value == 2) {
                state = is_archive[i].value;
                // 被选中的复选框的父节点
                var tddata = is_archive[i].parentNode.parentNode;
                // 遍历父节点下所有的字节点td
                $(tddata).find("td").each(function () {
                    // 当td的name等于某值时，获取该td下的内容
                    if (this.getAttribute("name") == "tname") {
                        content += this.innerHTML + "/";
                    }
                });
                content += "--"
            }
        }
    } else {
        layer.alert("请至少选择一条记录进行操作！");
        return;
    }

    if ($(":checkbox[name=is_open]:checked").size() != 0) {
        for (var i = 0; i < is_open.length; i++) {
            // 1 标志是勾选的是否开源
            if (is_open[i].checked && is_open[i].value == 1) {
                state = is_open[i].value;
                // 被选中的复选框的父节点
                var tddata = is_open[i].parentNode.parentNode;
                // 遍历父节点下所有的字节点td
                $(tddata).find("td").each(function () {
                    // 当td的name等于某值时，获取该td下的内容
                    if (this.getAttribute("name") == "tname") {
                        content += this.innerHTML + "/";
                    }
                });
                content += "--"
            }
        }
    } else {
        layer.alert("请至少选择一条记录进行操作！");
        return;
    }


    var split = content.split("--");
    var json = "";
    var path;
    for (var i = 0; i < split.length - 1; i++) {
        // 截取第二个/后面所有
        path = split[i].substr(split[i].indexOf("/", split[i].indexOf("/") + 1) + 1);
        path = path.substr(0, path.length - 1);
        // 拼接JSON
        json += path + "/" + state + ",";
    }
    var json_data = json.substr(0, json.length - 1);
    $.ajax({
        type: "POST",
        url: prefix + "/save",
        data: JSON.stringify(json_data), // 转JSON字符串
        contentType: "application/json;charset=utf-8",
        success: function (data) {
            if (data.code == 0) {
                layer.alert(data.msg);
                reLoad();
            }
        }, error: function () {
            layer.alert(data.msg);
        }
    });
}

function edit(id) {
    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id // iframe的url
    });
}

function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'id': id
            },
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function resetPwd(id) {
}

function batchRemove() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {

    });
}