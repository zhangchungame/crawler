function userresponse(a, b) {
    for (var c = b.slice(32), d = [], e = 0; e < c.length; e++) {
        var f = c.charCodeAt(e);
        d[e] = f > 57 ? f - 87 : f - 48
    }
    c = 36 * d[0] + d[1];
    var g = Math.round(a) + c; b = b.slice(0, 32);
    var h, i = [ [], [], [], [], [] ], j = {}, k = 0; e = 0;
    for (var l = b.length; e < l; e++)
        h = b.charAt(e), j[h] || (j[h] = 1, i[k].push(h), k++, k = 5 == k ? 0 : k);
    for (var m, n = g, o = 4, p = "", q = [1, 2, 5, 10, 50]; n > 0;)
        n - q[o] >= 0 ? (m = parseInt(Math.random() * i[o].length, 10), p += i[o][m], n -= q[o]) : (i.splice(o, 1), q.splice(o, 1), o -= 1);
    return p
}



function fromCharCode(json) {
    var bbb=[];
    for(var i=0;i<json.length;i++){
        bbb.push(String.fromCharCode(json[i]));
    }
    return bbb.join("");
}