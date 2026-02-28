param(
    [Parameter(Mandatory = $true)]
    [string] $Url
)
$headers = @{
    Upgrade = 'websocket'
    Connection = 'Upgrade'
    'Sec-WebSocket-Version' = '13'
    'Sec-WebSocket-Key' = 'dGhlIHNhbXBsZSBub25jZQ=='
}
try {
    $resp = Invoke-WebRequest -Method Get -Uri $Url -Headers $headers -TimeoutSec 5 -UseBasicParsing
    Write-Output ("HTTPStatus=" + $resp.StatusCode)
} catch {
    if ($_.Exception.Response -ne $null) {
        Write-Output ("HTTPError=" + $_.Exception.Response.StatusCode.value__)
    } else {
        Write-Output ("HTTPError=" + $_.Exception.Message)
    }
}
