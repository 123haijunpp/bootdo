var prefix = "/archiveCheck/tProject"
$(function () {

});

function load() {
    var path = $("#searchName").val();
    // "",null,undefined
    if (!$.trim(path)) {
        layer.alert("请输入需要扫描的路径!例如：C:\\Windows\\debug");
        return;
    }
    if ($.trim(path)) {
        path += "\\"
        // 判断文件夹路径格式是否正确
        var reg = /^[A-z]:\\(.+?\\)*$/;
        if (!reg.test(path)) {
            layer.alert("扫描的路径格式错误!", {icon: 2});
            return;
        }
        $("#exampleTable").bootstrapTable('destroy');
        $('#exampleTable')
            .bootstrapTable({
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/listArchiveCheck", // 服务器数据的加载地址
                showRefresh: true,
                showToggle: true,
                showColumns: true,
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
                        offset: params.offset,
                        path: $('#searchName').val()
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
                        field: 'proName',
                        title: '项目名字'
                    },
                    {
                        field: 'fileName',
                        title: '文件名'
                    },
                    {
                        field: 'path',
                        title: '路径'
                    }
                    , {
                        title: '操作',
                        field: 'state',
                        align: 'center',
                        formatter: function (value, row, index) {
                            return "<label  class='btn btn-primary btn-sm " + opens + "'><input type='checkbox' name='is_open' value='1' /> 开源 </label> " +
                                "  <label class='btn btn-warning btn-sm " + archives + "'><input  type='checkbox' name='is_archive' value='2'/> 归档 </label> ";
                        }
                    }
                ]
            });
        // 设置保存按钮，选择所有按钮可见
        $("#pull_left").show();
        $("#sp").show();
        $("#sp1").show();
        $("#sp2").show();

    }
}

function add() {
    // 获取是否开源的所有复选框
    var is_open = $("[name='is_open']");
    // 获取是否归档的所有复选框
    var is_archive = $("[name='is_archive']");
    var content = "";
    // 判断状态
    var state = 0;
    var is_archive_checked_size = $(":checkbox[name=is_archive]:checked").size();
    var is_open_checked_size = $(":checkbox[name=is_open]:checked").size();
    if (is_archive_checked_size && is_open_checked_size == 0) {
        for (var i = 0; i < is_archive.length; i++) {
            // 1 标志是勾选的是否开源
            if (is_archive[i].checked && is_archive[i].value == 2) {
                state = is_archive[i].value;
                // 被选中的复选框的父节点
                var tddata = is_archive[i].parentNode.parentNode.parentNode;
                // 遍历父节点下所有的字节点td的值
                $(tddata).find("td").each(function () {
                    content += this.innerHTML + "/";
                });
                content += "--"
            }
        }
    }

    if (is_open_checked_size && is_archive_checked_size == 0) {
        for (var i = 0; i < is_open.length; i++) {
            // 1 标志是勾选的是否开源
            if (is_open[i].checked && is_open[i].value == 1) {
                state = is_open[i].value;
                // 被选中的复选框的父节点
                var tddata = is_open[i].parentNode.parentNode.parentNode;
                // 遍历父节点下所有的字节点td的值
                $(tddata).find("td").each(function () {
                    content += this.innerHTML + "/";
                });
                content += "--"
            }
        }
    }


    console.log(content);
    // "",null,undefined
    if (!$.trim(content)) {
        layer.alert("请选择要保存的文件！", {icon: 2});
        return;
    }
    var split = content.split("--");
    var json = "";
    var path;
    /*
     * Python27/bz2.pyd/Python27/DLLs/bz2.pyd
     * /<label class="btn btn-primary btn-sm ">
     *     <input type="checkbox" name="is_open" value="1"> 开源 </label>
     * <label class="btn btn-warning btn-sm hidden">
     *         <input type="checkbox" name="is_archive" value="2"> 归档
     * </label> /
     * Python27/bz2.pyd/Python27/DLLs/bz2.pyd
     * /<label class="btn btn-primary btn-sm ">
     *     <input type="checkbox" name="is_open" value="1"> 开源 </label>
     * <label class="btn btn-warning btn-sm hidden">
     *     <input type="checkbox" name="is_archive" value="2"> 归档
     * </label> /
     */
    for (var i = 0; i < split.length - 1; i++) {
        path = split[i].substr(0, split[i].indexOf("/<"));
        json += path + "/" + state + ",";
    }
    var data_str = json.substr(0, json.length - 1);
    $.ajax({
        type: "POST",
        url: prefix + "/save",
        data: data_str,
        contentType: "application/json;charset=utf-8",
        success: function (data) {
            if (data.code == 0) {
                layer.alert(data.msg);
            }
        }, error: function () {
            layer.alert(data.msg);
        }
    });
}


/**
 * 全选择
 */
function checkAll() {
    var code_Values = document.all['is_open'];
    if (code_Values.length) {
        for (var i = 0; i < code_Values.length; i++) {
            code_Values[i].checked = true;
        }
    } else {
        code_Values.checked = true;
    }
    $("#sp").hide();
    $("#sp0").show();
}

//全不
function selectNone() {
    var code_Values = document.all['is_open'];
    if (code_Values.length) {
        for (var i = 0; i < code_Values.length; i++) {
            code_Values[i].checked = false;
        }
    } else {
        code_Values.checked = false;
    }
    $("#sp").show();
    $("#sp0").hide();
}


function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}

function dbLoad(state) {
    // 在初始化table之前，要将table销毁，否则会保留上次加载的内容
    $("#exampleTable").bootstrapTable('destroy');
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/openSourceList", // 服务器数据的加载地址
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
                pageNumber: 1, // 如果设置了分页，首页页码
                //search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParams: function (params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit: params.limit, //找多少条
                        offset: params.offset, //从数据库第几条记录开始
                        state: state
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
                        field: 'id',
                        title: 'id'
                    },
                    {
                        field: 'proName',
                        title: '项目名字'
                    },
                    {
                        field: 'path',
                        title: '路径'
                    }, {
                        field: 'fileName',
                        title: '文件名字'
                    }, {
                        field: 'state',
                        title: '状态',
                        formatter: function (value, row, index) {
                            var e = "";
                            if (value == "1") {
                                e = '已开源';
                            } else {
                                e = '已归档';
                            }
                            return e;
                        }
                    }
                ]
            });
}


/**
 *  已归档
 */
function archived() {
    dbLoad(2);
    // 设置保存按钮，选择所有按钮可见
    $("#pull_left").hide();
    $("#sp").hide();
}

/**
 * 已开源
 */
function open_source() {
    dbLoad(1);
    $("#pull_left").hide();
    $("#sp").hide();
}
