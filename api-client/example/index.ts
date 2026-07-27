import {
    ApiConfiguration,
    EmporixHttpClient,
    HelloWorldEvent,
    OEClient,
    OEConfig,
} from "../build/js/packages/emporix-api-client/kotlin/emporix-api-client.mjs"

const config: ApiConfiguration = {
    baseUrl: 'https://events.emporix.io/e/src_m74y2vqhlh66pp',
    tenant: 'fyaycff1'
}

const configOe: OEConfig = {
    baseUrl: 'https://events.emporix.io/',
    secret: '3%3QW1#D3*Z$',
    source: 'src_m74y2vqhlh66pp',
}
const http = new EmporixHttpClient();

const client = new OEClient(http, configOe)
const result = await client.publish(new HelloWorldEvent(
    '3', {
        test: ''
    },
));